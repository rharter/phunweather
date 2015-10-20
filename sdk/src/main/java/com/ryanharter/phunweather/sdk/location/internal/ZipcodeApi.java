package com.ryanharter.phunweather.sdk.location.internal;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface ZipcodeApi {

  @GET("/us/{zipcode}")
  Call<ZipcodeResult> lookupZipcode(@Path("zipcode") String zipcode);

}