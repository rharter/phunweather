package com.ryanharter.phunweather.sdk.internal;

import android.support.annotation.NonNull;
import com.ryanharter.phunweather.sdk.Callback;
import com.ryanharter.phunweather.sdk.LocationService;
import com.ryanharter.phunweather.sdk.model.Location;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

public class LocationServiceImpl implements LocationService {

  private final ZipcodeApi api;

  public LocationServiceImpl(ZipcodeApi api) {
    this.api = api;
  }

  @Override
  public void lookupZipcode(@NonNull final String zipcode, @NonNull final Callback<Location> callback) {
    Call<ZipcodeResult> call = api.lookupZipcode(zipcode);
    call.enqueue(new retrofit.Callback<ZipcodeResult>() {
      @Override public void onResponse(Response<ZipcodeResult> response, Retrofit retrofit) {
        ZipcodeResult result = response.body();

        // Ensure that some location was actually found
        if (result.places == null || result.places.isEmpty()) {
          // TODO make this a custom exception for "not found"
          callback.onResult(null, new IllegalArgumentException("Not location found for zipcode: "
              + zipcode));
          return;
        }

        // TODO why is places a collection? are multiples important?
        ZipcodeResult.Place place = result.places.get(0);

        // Make sure we handle number format exceptions via the callback, instead
        // of letting them bubble up the function call.
        Double latitude, longitude;
        try {
          latitude = Double.parseDouble(place.latitude);
          longitude = Double.parseDouble(place.longitude);
        } catch (NumberFormatException e) {
          callback.onResult(null, e);
          return;
        }

        Location location = Location.builder()
            .name(place.name)
            .zipcode(result.zipcode)
            .latitude(latitude)
            .longitude(longitude)
            .build();
        callback.onResult(location, null);
      }

      @Override public void onFailure(Throwable t) {
        callback.onResult(null, t);
      }
    });
  }
}
