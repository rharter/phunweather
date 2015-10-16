package com.ryanharter.phunweather;

import com.ryanharter.phunweather.inject.AppComponent;

public class TestPhunweatherApp extends PhunweatherApp {

  private AppComponent component;

  public void setComponent(AppComponent component) {
    this.component = component;
  }

  @Override public AppComponent component() {
    return component;
  }
}
