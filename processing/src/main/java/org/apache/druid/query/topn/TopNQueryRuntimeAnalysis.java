package org.apache.druid.query.topn;

import org.apache.druid.query.QueryRuntimeAnalysis;
import org.apache.druid.segment.ColumnValueSelector;
import org.apache.druid.segment.Cursor;

public class TopNQueryRuntimeAnalysis extends QueryRuntimeAnalysis<TopNQuery, TopNQueryMetrics> implements TopNQueryMetrics
{
  public TopNQueryRuntimeAnalysis(TopNQueryMetrics delegate)
  {
    super(delegate);
  }

  @Override
  public void threshold(TopNQuery query)
  {
    delegate.threshold(query);
  }

  @Override
  public void dimension(TopNQuery query)
  {
    delegate.dimension(query);
  }

  @Override
  public void numMetrics(TopNQuery query)
  {
    delegate.numMetrics(query);
  }

  @Override
  public void numComplexMetrics(TopNQuery query)
  {
    delegate.numComplexMetrics(query);
  }

  @Override
  public void granularity(TopNQuery query)
  {
    delegate.granularity(query);
  }

  @Override
  public void dimensionCardinality(int cardinality)
  {
    delegate.dimensionCardinality(cardinality);
  }

  @Override
  public void algorithm(TopNAlgorithm algorithm)
  {
    delegate.algorithm(algorithm);
  }

  @Override
  public void cursor(Cursor cursor)
  {
    delegate.cursor(cursor);
  }

  @Override
  public void columnValueSelector(ColumnValueSelector columnValueSelector)
  {
    delegate.columnValueSelector(columnValueSelector);
  }

  @Override
  public void numValuesPerPass(TopNParams params)
  {
    delegate.numValuesPerPass(params);
  }

  @Override
  public TopNQueryMetrics addProcessedRows(long numRows)
  {
    return delegate.addProcessedRows(numRows);
  }

  @Override
  public void startRecordingScanTime()
  {
    delegate.startRecordingScanTime();
  }

  @Override
  public TopNQueryMetrics stopRecordingScanTime()
  {
    return delegate.stopRecordingScanTime();
  }
}
