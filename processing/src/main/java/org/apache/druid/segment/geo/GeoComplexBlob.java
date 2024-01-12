package org.apache.druid.segment.geo;

import com.github.davidmoten.rtree.fbs.generated.PointDouble_;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.internal.PointDouble;
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

  private Geometry geometry;
  private byte[] bytes;

  public GeoComplexBlob(byte[] bytes)
  {
    this.bytes = bytes;
    // TODO
    this.geometry = PointDouble.create(10, 100);
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

  @Nullable
  public static GeoComplexBlob parse(@Nullable Object input, boolean reportParseExceptions)
  {
    if (input == null) {
      return null;
    }
    final GeoComplexBlob blob;
    if (input instanceof GeoComplexBlob) {
      return (GeoComplexBlob) input;
    }

    if (input instanceof String) {
      blob = GeoComplexBlob.ofString((String) input);
    } else {
      throw new IAE("Cannot handle [%s]", input.getClass());
    }
    // blob should not be null if we get to here, a null is a parse exception
    if (blob == null && reportParseExceptions) {
      throw new ParseException(input.toString(), "Cannot parse [%s] as an IP address", input);
    }
    return blob;
  }

  private static GeoComplexBlob ofString(String input)
  {
    return new GeoComplexBlob("[10, 2]".getBytes(StandardCharsets.UTF_8));
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

  public Geometry getGeometry()
  {
    return geometry;
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
