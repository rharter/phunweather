package com.ryanharter.phunweather.sdk.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.ryanharter.phunweather.sdk.common.Callback;
import com.ryanharter.phunweather.sdk.common.model.Forecast;
import com.ryanharter.phunweather.sdk.common.model.Location;
import com.ryanharter.phunweather.sdk.common.model.WeatherCollection;
import com.ryanharter.phunweather.sdk.common.model.WeatherData;
import com.ryanharter.phunweather.sdk.weather.internal.ForecastApi;
import com.ryanharter.phunweather.sdk.weather.internal.WeatherDbOpenHelper;
import com.ryanharter.phunweather.sdk.weather.internal.WeatherServiceImpl;
import com.squareup.moshi.Moshi;
import java.util.List;
import retrofit.MoshiConverterFactory;
import retrofit.Retrofit;

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
   * Retrieves the forecast for the provided {@link Location}.
   * @param location The location for which to receive the forecast.
   * @param cb Called with the list of stored locations.
   */
  void getForecast(@NonNull Location location, @NonNull Callback<Forecast> cb);

  /**
   * Provides the {@link WeatherService} implementation, creating
   * a new one if necessary.
   */
  class Factory {

    private static WeatherService service;

    /**
     * Creates a new {@link WeatherService} or returns the existing one.
     * @return a valid WeatherService.
     */
    @NonNull public static WeatherService create(@NonNull Context context, String apiKey) {
      if (service == null) {
        Moshi moshi = new Moshi.Builder()
            .add(Forecast.typeAdapterFactory())
            .add(WeatherData.typeAdapterFactory())
            .add(WeatherCollection.typeAdapterFactory())
            .build();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(new ForecastApi.ForecastBaseUrl(apiKey))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build();
        ForecastApi api = retrofit.create(ForecastApi.class);
        service = new WeatherServiceImpl(new WeatherDbOpenHelper(context.getApplicationContext()), api);
      }
      return service;
    }
  }

}
