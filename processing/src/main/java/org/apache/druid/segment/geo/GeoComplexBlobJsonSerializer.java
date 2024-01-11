package org.apache.druid.segment.geo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class GeoComplexBlobJsonSerializer extends JsonSerializer<GeoComplexBlob>
{
  @Override
  public void serialize(
      GeoComplexBlob geoComplexBlob,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException
  {
    jsonGenerator.writeBinary(geoComplexBlob.getBytes());
  }
}
