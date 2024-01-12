package org.apache.druid.segment.geo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.druid.data.input.impl.DimensionSchema;
import org.apache.druid.java.util.common.StringUtils;
import org.apache.druid.segment.column.ColumnType;

public class GeoComplexDimensionSchema extends DimensionSchema
{
  public static final String TYPE= GeoComplexTypeSerde.GEO_COMPLEX_TYPE;
  private GeometryType geometryType;
  @JsonCreator
  public GeoComplexDimensionSchema(@JsonProperty("name") String name, @JsonProperty("geometryType") String geometryType)
  {
    super(name, null, false);
    this.geometryType = geometryType == null ? GeometryType.ofDefault() : GeometryType.fromString(geometryType);
  }

  @Override
  public String getTypeName()
  {
    return GeoComplexTypeSerde.GEO_COMPLEX_TYPE;
  }

  @Override
  public ColumnType getColumnType()
  {
    return GeoSpatialModule.GEO_COMPLEX_TYPE;
  }

  @JsonProperty
  public GeometryType getGeometryType()
  {
    return geometryType;
  }

  public enum GeometryType {
    POINT,
    LINE,
    MULTIPOINT_LINE,
    POLYGON,
    REACTANGLE,
    CIRCLE;
    @Override
    @JsonValue
    public String toString()
    {
      return StringUtils.toUpperCase(name());
    }

    @JsonCreator
    public static GeometryType fromString(String name)
    {
      return name == null ? ofDefault() : valueOf(StringUtils.toUpperCase(name));
    }

    public static GeometryType ofDefault()
    {
      return POINT;
    }
    }
}
