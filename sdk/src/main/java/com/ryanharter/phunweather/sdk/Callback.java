package com.ryanharter.phunweather.sdk;

import android.support.annotation.Nullable;

/**
 * Communicates the response of the API call.
 *
 * @param <T> The type of result expected.
 */
public interface Callback<T> {

  /**
   * Called when the action is complete.  If successful, <code>result</code> contains
   * the resulting value, and <code>error</code> will be null.  If any error occurs,
   * <code>result</code> will be null and <code>error</code> contains the error value.
   *
   * @param result The successful result, or null.
   * @param error The error, or null.
   */
  void onResult(@Nullable T result, @Nullable Throwable error);

}
