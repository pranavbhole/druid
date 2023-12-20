package org.apache.druid.query;

public class DefaultGenericQueryRuntimeAnalysis<TQuery extends Query<?>>
    extends QueryRuntimeAnalysis<TQuery, DefaultQueryMetrics<TQuery>> implements QueryMetrics<TQuery>
{
  public DefaultGenericQueryRuntimeAnalysis(DefaultQueryMetrics<TQuery> delegate)
  {
    super(delegate);
  }
}
