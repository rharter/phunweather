package com.ryanharter.phunweather.sdk.weather.internal;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import com.ryanharter.phunweather.sdk.common.model.Location;

interface WeatherContract {

  abstract class Locations implements BaseColumns {
    static String TABLE = "Locations";
    static String NAME = "name";
    static String ZIPCODE = "zipcode";
    static String LATITUDE = "latitude";
    static String LONGITUDE = "longitude";

    static ContentValues toContentValues(Location location) {
      ContentValues values = new ContentValues();
      if (location.id() != null) {
        values.put(_ID, location.id());
      }
      values.put(NAME, location.name());
      values.put(ZIPCODE, location.zipcode());
      values.put(LATITUDE, location.latitude());
      values.put(LONGITUDE, location.longitude());
      return values;
    }

    static Location from(Cursor c) {
      return Location.builder()
          .id(c.getLong(c.getColumnIndex(_ID)))
          .name(c.getString(c.getColumnIndex(NAME)))
          .zipcode(c.getString(c.getColumnIndex(ZIPCODE)))
          .latitude(c.getDouble(c.getColumnIndex(LATITUDE)))
          .longitude(c.getDouble(c.getColumnIndex(LONGITUDE)))
          .build();
    }
  }
}
