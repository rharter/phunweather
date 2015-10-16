package com.ryanharter.phunweather.sdk.internal;

import com.squareup.moshi.Json;
import java.util.List;

class ZipcodeResult {
  @Json(name = "post code") String zipcode;
  String country;
  @Json(name = "country abbreviation") String countryAbbreviation;
  List<Place> places;

  public ZipcodeResult() {}

  public ZipcodeResult(String zipcode, String country, String countryAbbreviation,
      List<Place> places) {
    this.zipcode = zipcode;
    this.country = country;
    this.countryAbbreviation = countryAbbreviation;
    this.places = places;
  }

  static class Place {
    @Json(name = "place name") String name;
    String latitude;
    String longitude;
    String state;
    @Json(name = "state abbreviation") String stateAbbreviation;

    public Place() {}

    public Place(String name, String latitude, String longitude, String state,
        String stateAbbreviation) {
      this.name = name;
      this.latitude = latitude;
      this.longitude = longitude;
      this.state = state;
      this.stateAbbreviation = stateAbbreviation;
    }
  }
}
