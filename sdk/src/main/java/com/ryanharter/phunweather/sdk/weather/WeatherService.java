package com.ryanharter.phunweather.sdk.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.ryanharter.phunweather.sdk.Callback;
import com.ryanharter.phunweather.sdk.model.Location;
import com.ryanharter.phunweather.sdk.weather.internal.WeatherDatabase;
import com.ryanharter.phunweather.sdk.weather.internal.WeatherServiceImpl;
import java.util.List;

/**
 * Service for accessing weather data for locations.
 */
public interface WeatherService {

  /**
   * Stores the <code>location</code> for future use.
   * @param location The location to store.
   * @param cb The callback to be notified when the location has been stored.
   */
  void saveLocation(@NonNull Location location, @Nullable Callback<Location> cb);

  /**
   * Retrieves the list of stored locations.
   * @param cb Called with the list of stored locations.
   */
  void getLocations(@NonNull Callback<List<Location>> cb);

  /**
   * Provides the {@link WeatherService} implementation, creating
   * a new one if necessary.
   */
  class Factory {
    private static final String API_URL = "http://api.zippopotam.us";

    private static WeatherService service;

    /**
     * Creates a new {@link WeatherService} or returns the existing one.
     * @return a valid WeatherService.
     */
    @NonNull public static WeatherService create(@NonNull Context context) {
      if (service == null) {
        service = new WeatherServiceImpl(new WeatherDatabase(context.getApplicationContext()));
      }
      return service;
    }
  }

}
