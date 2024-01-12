package org.apache.druid.segment.geo;

import org.apache.druid.segment.column.ColumnBuilder;
import org.apache.druid.segment.column.ColumnCapabilitiesImpl;
import org.apache.druid.segment.column.ColumnType;
import org.apache.druid.segment.data.GenericIndexed;
import org.apache.druid.segment.data.ObjectStrategy;
import org.apache.druid.segment.nested.NestedDataComplexTypeSerde;
import org.apache.druid.segment.serde.ComplexColumnPartSupplier;
import org.apache.druid.segment.serde.ComplexMetricExtractor;
import org.apache.druid.segment.serde.ComplexMetricSerde;

import java.nio.ByteBuffer;

public class GeoComplexTypeSerde extends ComplexMetricSerde
{
  public static final String GEO_COMPLEX_TYPE = "geo";
  public static final GeoComplexTypeSerde INSTANCE = new GeoComplexTypeSerde();
  @Override
  public String getTypeName()
  {
    return GEO_COMPLEX_TYPE;
  }

  @Override
  public ComplexMetricExtractor getExtractor()
  {
    return null;
  }

  @Override
  public void deserializeColumn(ByteBuffer byteBuffer, ColumnBuilder columnBuilder)
  {
    final GenericIndexed column = GenericIndexed.read(byteBuffer, getObjectStrategy(), columnBuilder.getFileMapper());
    columnBuilder.setComplexColumnSupplier(new ComplexColumnPartSupplier(getTypeName(), column));
    final ColumnCapabilitiesImpl capabilitiesBuilder = columnBuilder.getCapabilitiesBuilder();
    capabilitiesBuilder.setDictionaryEncoded(false);
    capabilitiesBuilder.setHasSpatialIndexes(true);
  }

  @Override
  public ObjectStrategy getObjectStrategy()
  {
    return GeoComplexBlob.STRATEGY;
  }
}
