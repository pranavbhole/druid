package org.apache.druid.segment.geo;

import org.apache.druid.collections.spatial.RTree;
import org.apache.druid.io.Channels;
import org.apache.druid.java.util.common.io.smoosh.FileSmoosher;
import org.apache.druid.segment.ColumnValueSelector;
import org.apache.druid.segment.DimensionMergerV9;
import org.apache.druid.segment.IndexableAdapter;
import org.apache.druid.segment.column.ColumnDescriptor;
import org.apache.druid.segment.column.ValueType;
import org.apache.druid.segment.serde.ComplexColumnPartSerde;
import org.apache.druid.segment.serde.Serializer;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.List;

public class RTreeSpatialColumnMerger implements DimensionMergerV9
{
  public RTreeSpatialColumnMerger() {
  }

  @Override
  public void writeMergedValueDictionary(List<IndexableAdapter> adapters) throws IOException
  {

  }

  @Override
  public ColumnValueSelector convertSortedSegmentRowValuesToMergedRowValues(
      int segmentIndex,
      ColumnValueSelector source
  )
  {
    return null;
  }

  @Override
  public void processMergedRow(ColumnValueSelector selector) throws IOException
  {

  }

  @Override
  public void writeIndexes(@Nullable List<IntBuffer> segmentRowNumConversions) throws IOException
  {

  }

  @Override
  public boolean hasOnlyNulls()
  {
    return false;
  }

  @Override
  public ColumnDescriptor makeColumnDescriptor()
  {
    return new ColumnDescriptor.Builder()
        .setValueType(ValueType.COMPLEX)
        .setHasMultipleValues(false)
        .addSerde(
            ComplexColumnPartSerde.serializerBuilder()
                                  .withTypeName(GeoComplexTypeSerde.GEO_COMPLEX_TYPE)
                                  .withDelegate(new Serializer()
                                  {
                                    @Override
                                    public long getSerializedSize() throws IOException
                                    {
                                      // version
                                      long size = 1;
                                      return size;
                                    }

                                    @Override
                                    public void writeTo(
                                        WritableByteChannel channel,
                                        FileSmoosher smoosher
                                    ) throws IOException
                                    {

                                    }
                                  }).build()
        )
        .build();

  }
}
