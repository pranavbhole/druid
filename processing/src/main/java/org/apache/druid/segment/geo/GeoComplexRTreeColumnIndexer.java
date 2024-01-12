package org.apache.druid.segment.geo;

import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometry;
import org.apache.druid.collections.bitmap.BitmapFactory;
import org.apache.druid.collections.bitmap.MutableBitmap;
import org.apache.druid.query.dimension.DimensionSpec;
import org.apache.druid.query.monomorphicprocessing.RuntimeShapeInspector;
import org.apache.druid.segment.ColumnValueSelector;
import org.apache.druid.segment.DimensionIndexer;
import org.apache.druid.segment.DimensionSelector;
import org.apache.druid.segment.EncodedKeyComponent;
import org.apache.druid.segment.ObjectColumnSelector;
import org.apache.druid.segment.column.ColumnCapabilities;
import org.apache.druid.segment.column.ColumnCapabilitiesImpl;
import org.apache.druid.segment.data.CloseableIndexed;
import org.apache.druid.segment.incremental.IncrementalIndex;
import org.apache.druid.segment.incremental.IncrementalIndexRowHolder;

import javax.annotation.Nullable;


public class GeoComplexRTreeColumnIndexer implements DimensionIndexer<GeoComplexBlob, GeoComplexBlob, GeoComplexBlob>
{
  private RTree<GeoComplexBlob, Geometry> rTree;

  public GeoComplexRTreeColumnIndexer(int minChildrenPerNode, int maxChildrenPerNode)
  {
    this.rTree = RTree.minChildren(minChildrenPerNode).maxChildren(maxChildrenPerNode).create();
  }

  @Override
  public EncodedKeyComponent<GeoComplexBlob> processRowValsToUnsortedEncodedKeyComponent(
      @Nullable Object dimValues,
      boolean reportParseExceptions
  )
  {
   GeoComplexBlob geoComplexBlob =  GeoComplexBlob.parse(dimValues, reportParseExceptions);
   rTree.add(geoComplexBlob, geoComplexBlob.getGeometry());
   // TODO: calculate size delta
    return new EncodedKeyComponent<>(geoComplexBlob, geoComplexBlob.getBytes().length);
  }

  @Override
  public void setSparseIndexed()
  {

  }

  @Override
  public GeoComplexBlob getUnsortedEncodedValueFromSorted(GeoComplexBlob sortedIntermediateValue)
  {
    return sortedIntermediateValue;
  }

  @Override
  public CloseableIndexed<GeoComplexBlob> getSortedIndexedValues()
  {
    return null;
  }

  @Override
  public GeoComplexBlob getMinValue()
  {
    return null;
  }

  @Override
  public GeoComplexBlob getMaxValue()
  {
    return null;
  }

  @Override
  public int getCardinality()
  {
    return 0;
  }

  @Override
  public DimensionSelector makeDimensionSelector(
      DimensionSpec spec,
      IncrementalIndexRowHolder currEntry,
      IncrementalIndex.DimensionDesc desc
  )
  {
    return null;
  }

  @Override
  public ColumnValueSelector<?> makeColumnValueSelector(
      IncrementalIndexRowHolder currEntry,
      IncrementalIndex.DimensionDesc desc
  )
  {
    return new ObjectColumnSelector<GeoComplexBlob>() {
      @Nullable
      @Override
      public GeoComplexBlob getObject()
      {
        return null;
      }

      @Override
      public Class<? extends GeoComplexBlob> classOfObject()
      {
        return GeoComplexBlob.class;
      }

      @Override
      public void inspectRuntimeShape(RuntimeShapeInspector inspector)
      {

      }
    };
  }

  @Override
  public ColumnCapabilities getColumnCapabilities()
  {
    return ColumnCapabilitiesImpl.createDefault().setType(GeoSpatialModule.GEO_COMPLEX_TYPE);
  }

  @Override
  public int compareUnsortedEncodedKeyComponents(@Nullable GeoComplexBlob lhs, @Nullable GeoComplexBlob rhs)
  {
    return 0;
  }

  @Override
  public boolean checkUnsortedEncodedKeyComponentsEqual(@Nullable GeoComplexBlob lhs, @Nullable GeoComplexBlob rhs)
  {
    return false;
  }

  @Override
  public int getUnsortedEncodedKeyComponentHashCode(@Nullable GeoComplexBlob key)
  {
    return 0;
  }

  @Override
  public Object convertUnsortedEncodedKeyComponentToActualList(GeoComplexBlob key)
  {
    return null;
  }

  @Override
  public ColumnValueSelector convertUnsortedValuesToSorted(ColumnValueSelector selectorWithUnsortedValues)
  {
    return selectorWithUnsortedValues;
  }

  @Override
  public void fillBitmapsFromUnsortedEncodedKeyComponent(
      GeoComplexBlob key,
      int rowNum,
      MutableBitmap[] bitmapIndexes,
      BitmapFactory factory
  )
  {

  }
}
