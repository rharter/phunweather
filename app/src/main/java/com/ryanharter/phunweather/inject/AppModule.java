package com.ryanharter.phunweather.inject;

import android.content.Context;
import com.ryanharter.phunweather.sdk.LocationService;
import com.ryanharter.phunweather.sdk.weather.WeatherService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class AppModule {

  private final Context context;

  public AppModule(Context context) {
    this.context = context;
  }

  @Singleton @Provides public LocationService provideLocationService() {
    return LocationService.Factory.create();
  }

  @Singleton @Provides public WeatherService provideWeatherService() {
    return WeatherService.Factory.create(context);
  }

}
