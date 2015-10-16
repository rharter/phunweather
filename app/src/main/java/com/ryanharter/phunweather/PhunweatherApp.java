package com.ryanharter.phunweather;

import android.app.Application;
import com.ryanharter.phunweather.inject.AppComponent;
import com.ryanharter.phunweather.inject.AppModule;
import com.ryanharter.phunweather.inject.DaggerAppComponent;

public class PhunweatherApp extends Application {

  private AppComponent component;

  @Override public void onCreate() {
    super.onCreate();

    component = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .build();
  }

  public AppComponent component() {
    return component;
  }

}
