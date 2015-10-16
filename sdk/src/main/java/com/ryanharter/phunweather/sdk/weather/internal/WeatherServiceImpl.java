package com.ryanharter.phunweather.sdk.weather.internal;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.ryanharter.phunweather.sdk.Callback;
import com.ryanharter.phunweather.sdk.model.Location;
import com.ryanharter.phunweather.sdk.weather.WeatherService;
import com.ryanharter.phunweather.sdk.weather.internal.WeatherContract.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This implementation of the WeatherService API doesn't properly perform database
 * operations on a background thread.  In a real implementation, I would definitely
 * do that. :)
 */
public class WeatherServiceImpl implements WeatherService {

  private final WeatherDatabase db;

  public WeatherServiceImpl(WeatherDatabase db) {
    this.db = db;
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
}
