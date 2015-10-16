package com.ryanharter.phunweather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.ryanharter.phunweather.PhunweatherApp;
import com.ryanharter.phunweather.R;
import com.ryanharter.phunweather.fragments.LocationListFragment;
import com.ryanharter.phunweather.sdk.model.Location;
import com.ryanharter.phunweather.sdk.weather.WeatherService;
import javax.inject.Inject;

import static com.ryanharter.phunweather.activities.LocationPickerActivity.EXTRA_LOCATION;

public class WeatherActivity extends AppCompatActivity implements LocationListFragment.Listener {

  private static final int REQUEST_LOCATION = 0x1;

  @Inject WeatherService service;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((PhunweatherApp) getApplication()).component().inject(this);

    setContentView(R.layout.activity_weather);
  }

  @Override public void onAddNewLocation() {
    startActivityForResult(new Intent(this, LocationPickerActivity.class), REQUEST_LOCATION);
  }

  @Override public void onLocationClicked(Location location) {
    // TODO implement
    Toast.makeText(this, "Clicked: " + location.name(), Toast.LENGTH_SHORT).show();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_LOCATION && resultCode == RESULT_OK) {
      Location location = data.getParcelableExtra(EXTRA_LOCATION);
      service.saveLocation(location, null);
      return;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
}
