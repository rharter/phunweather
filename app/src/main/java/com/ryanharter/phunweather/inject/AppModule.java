package com.ryanharter.phunweather.inject;

import android.content.Context;
import com.ryanharter.phunweather.sdk.location.LocationService;
import com.ryanharter.phunweather.sdk.weather.WeatherService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class AppModule {

  private static final String API_KEY = "d7945a4157a0454fe141bbd8bbd9d521";

  private final Context context;

  public AppModule(Context context) {
    this.context = context;
  }

  @Singleton @Provides public LocationService provideLocationService() {
    return LocationService.Factory.create();
  }

  @Singleton @Provides public WeatherService provideWeatherService() {
    return WeatherService.Factory.create(context, API_KEY);
  }

}
