package com.ryanharter.phunweather.sdk.weather.internal;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.ryanharter.phunweather.sdk.common.Callback;
import com.ryanharter.phunweather.sdk.common.model.Forecast;
import com.ryanharter.phunweather.sdk.common.model.Location;
import com.ryanharter.phunweather.sdk.weather.WeatherService;
import com.ryanharter.phunweather.sdk.weather.internal.WeatherContract.Locations;
import java.util.ArrayList;
import java.util.List;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * This implementation of the WeatherService API doesn't properly perform database
 * operations on a background thread.  In a real implementation, I would definitely
 * do that. :)
 */
public class WeatherServiceImpl implements WeatherService {

  private final WeatherDatabase db;
  private final ForecastApi api;

  public WeatherServiceImpl(WeatherDatabase db, ForecastApi api) {
    this.db = db;
    this.api = api;
  }

  @Override public void saveLocation(@NonNull Location location, @Nullable Callback<Location> cb) {
    long id = db.insertOrUpdate(location);
    if (cb != null) {
      cb.onResult(location.toBuilder().id(id).build(), null);
    }
  }

  @Override public void getLocations(@NonNull Callback<List<Location>> cb) {
    Cursor c = db.listLocations();
    if (c == null) {
      cb.onResult(new ArrayList<Location>(), null);
      return;
    }
    List<Location> locations = new ArrayList<>(c.getCount());
    while (c.moveToNext()) {
      locations.add(Locations.from(c));
    }
    cb.onResult(locations, null);
  }

  @Override public void getForecast(@NonNull Location location, @NonNull final Callback<Forecast> cb) {
    Call<Forecast> call = api.getForecast(location.latitude(), location.longitude());
    call.enqueue(new retrofit.Callback<Forecast>() {
      @Override public void onResponse(Response<Forecast> response, Retrofit retrofit) {
        if (response.code() != 200) {
          cb.onResult(null, new Exception("HTTP Error: " + response.code()));
        } else {
          cb.onResult(response.body(), null);
        }
      }

      @Override public void onFailure(Throwable t) {
        cb.onResult(null, t);
      }
    });
  }
}
