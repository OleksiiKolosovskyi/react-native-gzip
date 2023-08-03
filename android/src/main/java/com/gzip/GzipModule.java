package com.gzip;

import androidx.annotation.NonNull;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import com.facebook.react.bridge.ReadableArray;

@ReactModule(name = GzipModule.NAME)
public class GzipModule extends ReactContextBaseJavaModule {
  public static final String NAME = "Gzip";
  public static final String ER_FAILURE = "ERROR_FAILED";

  public GzipModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  /**
   * Decompress from base64 to string.
   */
  @ReactMethod
  public void inflate(@NonNull final String data, @NonNull final Promise promise) {
    try {
      final byte[] inputBytes = Base64.decode(data, Base64.DEFAULT);
      final byte[] outputBytes = decompress(inputBytes);
      final String outputString = new String(outputBytes, "UTF-8");

      promise.resolve(outputString);
    } catch (final Throwable ex) {
      promise.reject(ER_FAILURE, ex);
    }
  }

  /**
   * Compress string to base64.
   */
  @ReactMethod
  public void deflate(@NonNull final String data, @NonNull final Promise promise) {
    try {
      final byte[] inputBytes = data.getBytes("UTF-8");
      final byte[] outputBytes = compress(inputBytes);
      final String outputString = Base64.encodeToString(outputBytes, Base64.NO_WRAP);

      promise.resolve(outputString);
    } catch (final Throwable ex) {
      promise.reject(ER_FAILURE, ex);
    }
  }

  /**
   * Decompress from base64 to base64.
   */
  @ReactMethod
  public void inflateBase64(@NonNull final String data, @NonNull final Promise promise) {
    try {
      final byte[] inputBytes = Base64.decode(data, Base64.DEFAULT);
      final byte[] outputBytes = decompress(inputBytes);
      final String outputString = Base64.encodeToString(outputBytes, Base64.NO_WRAP);

      promise.resolve(outputString);
    } catch (final Throwable ex) {
      promise.reject(ER_FAILURE, ex);
    }
  }

  /**
   * Compress base64 to base64.
   */
  @ReactMethod
  public void deflateBase64(@NonNull final String data, @NonNull final Promise promise) {
    try {
      final byte[] inputBytes = Base64.decode(data, Base64.DEFAULT);
      final byte[] outputBytes = compress(inputBytes);
      final String outputString = Base64.encodeToString(outputBytes, Base64.NO_WRAP);

      promise.resolve(outputString);
    } catch (final Throwable ex) {
      promise.reject(ER_FAILURE, ex);
    }
  }

  /*
   * Compress
   */
  public static byte[] compressBytes(byte[] data) throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    GZIPOutputStream gos = new GZIPOutputStream(os);

    gos.write(data);
    gos.close();

    byte[] compressed = os.toByteArray();

    os.close();

    return compressed;
  }

  /*
   * Decompress
   */
  public static byte[] decompressBytes(byte[] compressed) throws IOException {
    ByteArrayInputStream is = new ByteArrayInputStream(compressed);
    GZIPInputStream gis = new GZIPInputStream(is);
    ByteArrayOutputStream os = new ByteArrayOutputStream();

    byte[] buffer = new byte[1024];
    int len;

    while ((len = gis.read(buffer)) != -1) {
      os.write(buffer, 0, len);
    }

    gis.close();
    is.close();

    return os.toByteArray();
  }
}
