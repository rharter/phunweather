package com.ryanharter.phunweather.sdk.weather.internal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ryanharter.phunweather.sdk.common.model.Location;
import com.ryanharter.phunweather.sdk.weather.internal.WeatherContract.Locations;

public class WeatherDbOpenHelper extends SQLiteOpenHelper {

  private static final int VERSION = 1;

  public WeatherDbOpenHelper(Context context) {
    super(context, "weather.db", null /* cursorFactory */, VERSION);
  }

  private static final String CREATE_LOCATION = ""
      + "CREATE TABLE " + Locations.TABLE + "("
      + Locations._ID + " INTEGER NOT NULL PRIMARY KEY,"
      + Locations.NAME + " TEXT NOT NULL,"
      + Locations.ZIPCODE + " TEXT NOT NULL,"
      + Locations.LATITUDE + " REAL NOT NULL,"
      + Locations.LONGITUDE + " REAL NOT NULL"
      + ")";

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_LOCATION);

    // Insert sample data
    db.insert(Locations.TABLE, null, Locations.toContentValues(Location.builder()
            .name("Bolingbrook")
            .zipcode("60440")
            .latitude(41.6976)
            .longitude(-88.0873)
            .build())
    );
    db.insert(Locations.TABLE, null, Locations.toContentValues(Location.builder()
            .name("Austin")
            .zipcode("78757")
            .latitude(30.3437)
            .longitude(-97.7316)
            .build())
    );
    db.insert(Locations.TABLE, null, Locations.toContentValues(Location.builder()
            .name("Beverly Hills")
            .zipcode("90210")
            .latitude(34.0901)
            .longitude(-118.4065)
            .build())
    );
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
