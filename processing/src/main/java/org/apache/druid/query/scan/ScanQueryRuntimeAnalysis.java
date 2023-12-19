package org.apache.druid.query.scan;

import org.apache.druid.query.QueryRuntimeAnalysis;

public class ScanQueryRuntimeAnalysis extends QueryRuntimeAnalysis<ScanQuery, ScanQueryMetrics>
    implements ScanQueryMetrics
{
  public ScanQueryRuntimeAnalysis(ScanQueryMetrics delegate)
  {
    super(delegate);
  }

  @Override
  public void limit(ScanQuery query)
  {

  }

  @Override
  public void numMetrics(ScanQuery query)
  {

  }

  @Override
  public void numComplexMetrics(ScanQuery query)
  {

  }

  @Override
  public void granularity(ScanQuery query)
  {

  }
}
