package org.apache.druid.segment.geo;

import org.apache.druid.segment.column.ComplexColumn;
import org.apache.druid.segment.column.GenericIndexedBasedComplexColumn;
import org.apache.druid.segment.data.GenericIndexed;

public class GeoComplexColumn extends GenericIndexedBasedComplexColumn
{
  public GeoComplexColumn(String typeName, GenericIndexed<?> index)
  {
    super(typeName, index);
  }
}
