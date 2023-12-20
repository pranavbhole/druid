package org.apache.druid.query.timeseries;

import org.apache.druid.query.QueryRuntimeAnalysis;

public class TimeseriesQueryRuntimeAnalysis extends QueryRuntimeAnalysis<TimeseriesQuery, TimeseriesQueryMetrics>
    implements TimeseriesQueryMetrics
{
  public TimeseriesQueryRuntimeAnalysis(TimeseriesQueryMetrics delegate)
  {
    super(delegate);
  }

  @Override
  public void limit(TimeseriesQuery query)
  {
    delegate.limit(query);
  }

  @Override
  public void numMetrics(TimeseriesQuery query)
  {
    delegate.numMetrics(query);
  }

  @Override
  public void numComplexMetrics(TimeseriesQuery query)
  {
    delegate.numComplexMetrics(query);
  }

  @Override
  public void granularity(TimeseriesQuery query)
  {
    delegate.granularity(query);
  }
}
