package com.ryanharter.phunweather.sdk.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

/**
 * Represents a location for which the user can retrieve weather information.
 */
@AutoValue public abstract class Location implements Parcelable {

  @Nullable public abstract Long id();
  public abstract String name();
  public abstract String zipcode();
  public abstract double latitude();
  public abstract double longitude();

  public static Builder builder() {
    return new AutoValue_Location.Builder();
  }

  public Builder toBuilder() {
    return new AutoValue_Location.Builder(this);
  }

  @Override public int describeContents() {
    return 0;
  }

  /**
   * Helper class to build new {@link Location} objects.
   */
  @AutoValue.Builder public abstract static class Builder {
    public abstract Builder id(@Nullable Long id);
    public abstract Builder name(String name);
    public abstract Builder zipcode(String zipcode);
    public abstract Builder latitude(double latitude);
    public abstract Builder longitude(double longitude);
    public abstract Location build();
  }
}
