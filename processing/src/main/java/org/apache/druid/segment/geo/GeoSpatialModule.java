package org.apache.druid.segment.geo;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Binder;
import org.apache.druid.initialization.DruidModule;
import org.apache.druid.segment.DimensionHandlerUtils;
import org.apache.druid.segment.column.ColumnType;
import org.apache.druid.segment.serde.ComplexMetrics;

import java.util.Collections;
import java.util.List;

public class GeoSpatialModule implements DruidModule
{
  public static final ColumnType GEO_COMPLEX_TYPE = ColumnType.ofComplex(GeoComplexTypeSerde.GEO_COMPLEX_TYPE);
  @Override
  public List<? extends Module> getJacksonModules()
  {
    return Collections.singletonList(
        new SimpleModule("GeoSpatialModule")
            .registerSubtypes(
                new NamedType(GeoComplexDimensionSchema.class, GeoComplexTypeSerde.GEO_COMPLEX_TYPE)
            )
            .addSerializer(GeoComplexBlob.class, new GeoComplexBlobJsonSerializer())
    );
  }
  @Override
  public void configure(Binder binder)
  {
    registerHandlersAndSerde();
  }
  @VisibleForTesting
  public static void registerHandlersAndSerde()
  {
    if (ComplexMetrics.getSerdeForType(GeoComplexTypeSerde.GEO_COMPLEX_TYPE) == null) {
      ComplexMetrics.registerSerde(GeoComplexTypeSerde.GEO_COMPLEX_TYPE, GeoComplexTypeSerde.INSTANCE);
    }
    DimensionHandlerUtils.registerDimensionHandlerProvider(
        GeoComplexTypeSerde.GEO_COMPLEX_TYPE,
        GeoComplexDimensionHandler::new
    );
  }
}
