package com.ryanharter.phunweather.sdk.weather.internal;

import com.ryanharter.phunweather.sdk.common.model.Forecast;
import com.squareup.okhttp.HttpUrl;
import retrofit.BaseUrl;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface ForecastApi {

  // This should really be using a different, internal, model object than
  // the exposed one, so that we don't have the change the exposed object
  // we change the underlying service.
  @GET("{lat},{long}")
  Call<Forecast> getForecast(@Path("lat") double latitude, @Path("long") double longitude);

  final class ForecastBaseUrl implements BaseUrl {
    private static final String BASE_URL_FORMAT = "https://api.forecast.io/forecast/%s/";

    private final HttpUrl url;

    public ForecastBaseUrl(String apiKey) {
      this.url = HttpUrl.parse(String.format(BASE_URL_FORMAT, apiKey));
    }

    @Override public HttpUrl url() {
      return url;
    }
  }

}
