package com.ryanharter.phunweather.sdk.location;

import android.support.annotation.NonNull;
import com.ryanharter.phunweather.sdk.common.Callback;
import com.ryanharter.phunweather.sdk.common.model.Location;
import com.ryanharter.phunweather.sdk.location.internal.LocationServiceImpl;
import com.ryanharter.phunweather.sdk.location.internal.ZipcodeApi;
import retrofit.MoshiConverterFactory;
import retrofit.Retrofit;

/**
 * Location lookup service used to determine a {@link Location} based on minimal inputs
 * (currently only zipcode).
 */
public interface LocationService {

  /**
   * Looks up a location by zip code.
   * <p>
   * This request initiates a network request.
   *
   * @param zipcode The zipcode used to identify the location.
   * @param callback Called on success with the identified location.
   */
  void lookupZipcode(@NonNull String zipcode, @NonNull Callback<Location> callback);

  /**
   * Provides the {@link LocationService} implementation, creating
   * a new one if necessary.
   */
  class Factory {
    private static final String API_URL = "http://api.zippopotam.us";

    private static LocationService service;

    @NonNull public static LocationService create() {
      if (service == null) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build();
        ZipcodeApi api = retrofit.create(ZipcodeApi.class);
        service = new LocationServiceImpl(api);
      }
      return service;
    }
  }
}
