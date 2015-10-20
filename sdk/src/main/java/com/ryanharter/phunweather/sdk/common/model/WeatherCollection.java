package com.ryanharter.phunweather.sdk.common.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import java.util.List;

@AutoValue public abstract class WeatherCollection {

  public abstract String summary();
  public abstract String icon();
  public abstract List<WeatherData> data();

  public static Builder builder() {
    return new AutoValue_WeatherCollection.Builder();
  }

  public static JsonAdapter.Factory typeAdapterFactory() {
    return AutoValue_WeatherCollection.typeAdapterFactory();
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder summary(String summary);
    public abstract Builder icon(String icon);
    public abstract Builder data(List<WeatherData> data);
    public abstract WeatherCollection build();
  }
}
