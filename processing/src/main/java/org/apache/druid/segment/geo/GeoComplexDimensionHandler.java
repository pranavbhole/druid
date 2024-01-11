package org.apache.druid.segment.geo;

import org.apache.druid.data.input.impl.DimensionSchema;
import org.apache.druid.java.util.common.io.Closer;
import org.apache.druid.segment.ColumnValueSelector;
import org.apache.druid.segment.DimensionHandler;
import org.apache.druid.segment.DimensionIndexer;
import org.apache.druid.segment.DimensionMergerV9;
import org.apache.druid.segment.IndexSpec;
import org.apache.druid.segment.ProgressIndicator;
import org.apache.druid.segment.column.ColumnCapabilities;
import org.apache.druid.segment.selector.settable.SettableColumnValueSelector;
import org.apache.druid.segment.writeout.SegmentWriteOutMedium;

import java.util.Comparator;

public class GeoComplexDimensionHandler implements DimensionHandler<GeoComplexBlob, GeoComplexBlob, GeoComplexBlob>
{
  private final String name;
  //private final GeoComplexDimensionSchema.GeometryType geometryType;
  public GeoComplexDimensionHandler(String name) {
    this.name = name;
    //this.geometryType = geometryType;
  }
  @Override
  public String getDimensionName()
  {
    return name;
  }

  @Override
  public DimensionSchema getDimensionSchema(ColumnCapabilities capabilities)
  {
    return new GeoComplexDimensionSchema(name, GeoComplexDimensionSchema.GeometryType.POINT, capabilities.hasBitmapIndexes());
  }

  @Override
  public DimensionIndexer<GeoComplexBlob, GeoComplexBlob, GeoComplexBlob> makeIndexer(boolean useMaxMemoryEstimates)
  {
    return null;
  }

  @Override
  public DimensionMergerV9 makeMerger(
      IndexSpec indexSpec,
      SegmentWriteOutMedium segmentWriteOutMedium,
      ColumnCapabilities capabilities,
      ProgressIndicator progress,
      Closer closer
  )
  {
    return null;
  }

  @Override
  public int getLengthOfEncodedKeyComponent(GeoComplexBlob dimVals)
  {
    return 0;
  }

  @Override
  public Comparator<ColumnValueSelector> getEncodedValueSelectorComparator()
  {
    return null;
  }

  @Override
  public SettableColumnValueSelector makeNewSettableEncodedValueSelector()
  {
    return null;
  }
}
