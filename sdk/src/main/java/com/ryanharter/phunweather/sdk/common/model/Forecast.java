package com.ryanharter.phunweather.sdk.common.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue public abstract class Forecast {

  public abstract double latitude();
  public abstract double longitude();
  public abstract String timezone();
  public abstract int offset();
  public abstract WeatherData currently();
  public abstract WeatherCollection hourly();
  public abstract WeatherCollection daily();

  public static Builder builder() {
    return new AutoValue_Forecast.Builder();
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_Forecast.typeAdapterFactory();
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder latitude(double latitude);
    public abstract Builder longitude(double longitude);
    public abstract Builder timezone(String timezone);
    public abstract Builder offset(int offset);
    public abstract Builder currently(WeatherData currently);
    public abstract Builder hourly(WeatherCollection hourly);
    public abstract Builder daily(WeatherCollection daily);
    public abstract Forecast build();
  }

}
