package com.ryanharter.phunweather.sdk.common.model;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue public abstract class WeatherData {
  public abstract long time();
  public abstract String summary();
  public abstract String icon();
  @Nullable public abstract Double precipIntensity();
  @Nullable public abstract Double precipProbability();
  @Nullable public abstract Double temperature();
  @Nullable public abstract Double apparentTemperature();
  @Nullable public abstract Double dewPoint();
  @Nullable public abstract Double humidity();
  @Nullable public abstract Double windSpeed();
  @Nullable public abstract Double windBearing();
  @Nullable public abstract Double visibility();
  @Nullable public abstract Double cloudCover();
  @Nullable public abstract Double pressure();
  @Nullable public abstract Double ozone();

  public static Builder builder() {
    return new AutoValue_WeatherData.Builder();
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_WeatherData.typeAdapterFactory();
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder time(long time);
    public abstract Builder summary(String summary);
    public abstract Builder icon(String icon);
    public abstract Builder precipIntensity(Double precipIntensity);
    public abstract Builder precipProbability(Double precipProbability);
    public abstract Builder temperature(Double temperature);
    public abstract Builder apparentTemperature(Double apparentTemperature);
    public abstract Builder dewPoint(Double dewPoint);
    public abstract Builder humidity(Double humidity);
    public abstract Builder windSpeed(Double windSpeed);
    public abstract Builder windBearing(Double windBearing);
    public abstract Builder visibility(Double visibility);
    public abstract Builder cloudCover(Double cloudCover);
    public abstract Builder pressure(Double pressure);
    public abstract Builder ozone(Double ozone);
    public abstract WeatherData build();
  }

}
