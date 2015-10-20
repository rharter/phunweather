package com.ryanharter.phunweather.inject;

import com.ryanharter.phunweather.activities.LocationPickerActivity;
import com.ryanharter.phunweather.activities.WeatherActivity;
import com.ryanharter.phunweather.fragments.LocationListFragment;
import com.ryanharter.phunweather.fragments.WeatherDetailFragment;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = AppModule.class) public interface AppComponent {
  void inject(LocationPickerActivity locationPickerActivity);
  void inject(WeatherActivity weatherActivity);
  void inject(LocationListFragment locationListFragment);
  void inject(WeatherDetailFragment weatherDetailFragment);
}
