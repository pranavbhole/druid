/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { Button, Classes, Dialog } from '@blueprintjs/core';
import * as echarts from 'echarts';
import * as JSONBig from 'json-bigint-native';
import type { JSX } from 'react';
import React, { useEffect, useLayoutEffect, useMemo, useRef, useState } from 'react';

import { Loader } from '../../../components';
import type { DruidEngine, QueryContext, QueryWithContext } from '../../../druid-models';
import { isEmptyContext } from '../../../druid-models';
import { useQueryManager, useResizeObserver } from '../../../hooks';
import type { QueryExplanation } from '../../../utils';
import { nonEmptyArray } from '../../../utils';

//import { debugData } from './debug-data.mock';

import './explain-and-analyse-dialog.scss';

function isExplainQuery(query: string): boolean {
  return /^\s*EXPLAIN\sANALYSE/im.test(query);
}

function wrapInExplainIfNeeded(query: string): string {
  if (isExplainQuery(query)) return query;
  return `${query}`;
}

export interface QueryContextEngine extends QueryWithContext {
  engine: DruidEngine;
}

export interface ExplainAndAnalyseDialogProps {
  queryWithContext: QueryContextEngine;
  mandatoryQueryContext?: Record<string, any>;
  onClose: () => void;
  openQueryLabel: string | undefined;
  onOpenQuery?: (queryString: string) => void;
}

export const ExplainAndAnalyseDialog = React.memo(function ExplainAndAnalyseDialog(
  props: ExplainAndAnalyseDialogProps,
) {
  const { queryWithContext, onClose, mandatoryQueryContext } = props;

  const [selectedItem, setSelectedItem] = useState<any>(undefined);

  const [explainState] = useQueryManager<QueryContextEngine, QueryExplanation[] | string>({
    processQuery: async queryWithContext => {
      const { queryContext, wrapQueryLimit, queryString } = queryWithContext;

      let context: QueryContext | undefined;
      if (!isEmptyContext(queryContext) || wrapQueryLimit || mandatoryQueryContext) {
        context = {
          ...queryContext,
          ...(mandatoryQueryContext || {}),
          useNativeQueryExplain: true,
          analyze: true,
        };
        if (typeof wrapQueryLimit !== 'undefined') {
          context.sqlOuterLimit = wrapQueryLimit + 1;
        }
      }

      const payload: any = {
        query: wrapInExplainIfNeeded(queryString),
        context,
      };
      const response = await fetch("/druid/v2/sql", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        throw new Error(`Network response was not ok, status: ${response.status}`);
      }
      const responseBody = await response.json();
      return [responseBody];
    },
    initQuery: queryWithContext,
  });

  let content: JSX.Element;

  const { loading, error: explainError, data: explainResult } = explainState;

  if (loading) {
    content = <Loader />;
  } else if (explainError) {
    content = <div>{explainError.message}</div>;
  } else if (!explainResult) {
    content = <div />;
  } else if (nonEmptyArray(explainResult)) {
    if (explainResult.length === 1) {
      content = (
        <div className="query-explanation">
          <Dendrogram
            data={explainResult[0]}
            onItemClick={item => setSelectedItem(item)}
            selectedItem={selectedItem}
          />
          <ItemDetail item={selectedItem} />
        </div>
      );
    } else {
      content = <div />;
    }
  } else {
    content = <div className="generic-result">{String(explainResult)}</div>;
  }

  return (
    <Dialog className="explain-and-analyse-dialog" isOpen onClose={onClose} title="Query analysis">
      <div className={Classes.DIALOG_BODY}>{content}</div>
      <div className={Classes.DIALOG_FOOTER}>
        <div className={Classes.DIALOG_FOOTER_ACTIONS}>
          <Button text="Close" onClick={onClose} />
        </div>
      </div>
    </Dialog>
  );
});

const ItemDetail = (props: any) => {
  const { item } = props;

  if (!item) return <div className="item-detail" />;

  return (
    <div className="item-detail">
      <pre>{JSONBig.stringify(item, undefined, 2)}</pre>
    </div>
  );
};

function augmentData(data: any[], selectedItem?: any) {
  const result: any[] = [];

  const totalWaitTime = data.reduce((acc, curr) => acc + curr.metrics['query/time'], 0);

  data.forEach((node: any) => {
    result.push({
      name: node.debugInfo?.server ?? '',
      node,
      itemStyle: {
        color:
          // not sure if relying on the server name is a good idea
          selectedItem && selectedItem.debugInfo?.server === node.debugInfo?.server
            ? 'red'
            : 'green',
      },
      value: node.metrics['query/time'] / totalWaitTime,
      tooltip: { metrics: node.metrics, debugInfo: node.debugInfo },
      children: augmentData(node.children ?? [], selectedItem),
    });
  });

  return result;
}

interface DendrogramProps {
  data: any;
  onItemClick: (item: any) => void;
  selectedItem?: any;
}

const Dendrogram = (props: DendrogramProps) => {
  const { data, onItemClick, selectedItem } = props;

  const myChart = useRef<echarts.EChartsType | undefined>();
  const container = useRef<HTMLDivElement | null>(null);

  const { width, height } = useResizeObserver(document.body);
  useLayoutEffect(() => {
    if (!myChart.current) return;
    myChart.current.resize();
  }, [width, height]);

  useEffect(() => {
    if (!container.current) return;
    myChart.current = echarts.init(container.current, 'dark');

    return () => {
      myChart.current?.dispose();
    };
  }, []);

  useEffect(() => {
    if (!myChart.current) return;

    const fn = (params: any) => {
      onItemClick(params.data.node);
    };

    myChart.current.on('click', fn);

    return () => {
      myChart.current?.off('click', fn);
    };
  }, [onItemClick]);

  const augmentedData = useMemo(() => augmentData([data], selectedItem), [data, selectedItem]);

  useEffect(() => {
    myChart.current?.setOption({
      backgroundColor: 'transparent',
      // tooltip: {
      //   trigger: 'item',
      //   triggerOn: 'mousemove',
      //   formatter: (params: any) => {
      //     const { data } = params;
      //     return `<pre style="font-size: 10px">${JSONBig.stringify(
      //       data?.tooltip,
      //       undefined,
      //       2,
      //     )}</pre>`;
      //   },
      //   position: function (pos: any, _params: any, _dom: any, _rect: any, size: any) {
      //     // tooltip will be fixed on the right if mouse hovering on the left,
      //     // and on the left if hovering on the right.
      //     const obj: any = { top: 60 };
      //     obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 5;
      //     return obj;
      //   },
      // },
      series: [
        {
          type: 'tree',

          data: augmentedData,

          top: '1%',
          left: '7%',
          bottom: '10%',
          right: '20%',

          symbolSize: (_value: number, params: any) => {
            // root node
            if (!params.data.value) return 7;

            return params.data.value * 25;
          },

          label: {
            position: 'left',
            verticalAlign: 'middle',
            align: 'right',
            fontSize: 11,
            borderColor: 'none',
          },

          leaves: {
            label: {
              position: 'right',
              verticalAlign: 'middle',
              align: 'left',
            },
          },

          emphasis: {
            focus: 'descendant',
          },

          expandAndCollapse: false,
          animationDuration: 550,
          animationDurationUpdate: 750,
          initialTreeDepth: -1, // all nodes expanded
        },
      ],
    });
  }, [data, augmentedData]);

  return <div className="dendrogram" ref={container} />;
};
