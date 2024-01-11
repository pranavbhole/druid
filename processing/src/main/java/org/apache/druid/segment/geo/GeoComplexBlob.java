package org.apache.druid.segment.geo;

import com.github.davidmoten.rtree.geometry.Geometry;
import com.google.gson.GsonBuilder;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import org.apache.commons.math3.geometry.Point;
import org.apache.druid.java.util.common.IAE;
import org.apache.druid.java.util.common.guava.Comparators;
import org.apache.druid.java.util.common.parsers.ParseException;
import org.apache.druid.segment.data.ObjectStrategy;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;

public class GeoComplexBlob implements Comparable<GeoComplexBlob>
{
  public static final int SIZE = 16;
  public static final Comparator<GeoComplexBlob> COMPARATOR = Comparators.naturalNullsFirst();

  public static final Strategy STRATEGY = new Strategy();

//  private Geometry geometry;
  private byte[] bytes;

  public GeoComplexBlob(byte[] bytes)
  {
    this.bytes = bytes;
  }

  @Nullable
  public static GeoComplexBlob ofByteBuffer(final ByteBuffer blob)
  {
    if (blob != null) {
      byte[] bytes = new byte[SIZE];
      final int oldPosition = blob.position();
      blob.get(bytes, 0, SIZE);
      blob.position(oldPosition);
      return new GeoComplexBlob(bytes);
    }
    return null;
  }

  public byte[] getBytes()
  {
    return bytes;
  }

  @Override
  public int compareTo(GeoComplexBlob o)
  {
    return ByteBuffer.wrap(getBytes()).compareTo(ByteBuffer.wrap(o.getBytes()));
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeoComplexBlob that = (GeoComplexBlob) o;
    return Arrays.equals(bytes, that.bytes);
  }

  @Override
  public int hashCode()
  {
    return Arrays.hashCode(bytes);
  }

  @Override
  public String toString()
  {
    return "geoComplex";
  }

  public static class Strategy implements ObjectStrategy<GeoComplexBlob>
  {
    @Override
    public Class<? extends GeoComplexBlob> getClazz()
    {
      return GeoComplexBlob.class;
    }

    @Nullable
    @Override
    public GeoComplexBlob fromByteBuffer(ByteBuffer buffer, int numBytes)
    {
      return GeoComplexBlob.ofByteBuffer(buffer);
    }

    @Nullable
    @Override
    public byte[] toBytes(@Nullable GeoComplexBlob val)
    {
      if (val == null) {
        return null;
      }
      return val.getBytes();
    }

    @Override
    public int compare(GeoComplexBlob o1, GeoComplexBlob o2)
    {
      return COMPARATOR.compare(o1, o2);
    }
  }

}
