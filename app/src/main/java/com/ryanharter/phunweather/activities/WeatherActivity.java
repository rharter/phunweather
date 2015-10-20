package com.ryanharter.phunweather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.ryanharter.phunweather.PhunweatherApp;
import com.ryanharter.phunweather.R;
import com.ryanharter.phunweather.fragments.LocationListFragment;
import com.ryanharter.phunweather.fragments.WeatherDetailFragment;
import com.ryanharter.phunweather.sdk.common.Callback;
import com.ryanharter.phunweather.sdk.common.model.Location;
import com.ryanharter.phunweather.sdk.weather.WeatherService;
import javax.inject.Inject;

import static com.ryanharter.phunweather.activities.LocationPickerActivity.EXTRA_LOCATION;

public class WeatherActivity extends AppCompatActivity implements LocationListFragment.Listener {

  private static final int REQUEST_LOCATION = 0x1;
  private static final String FRAGMENT_DETAIL = "detail";
  private static final String TAG = WeatherActivity.class.getSimpleName();

  @Inject WeatherService service;

  LocationListFragment locationListFragment;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((PhunweatherApp) getApplication()).component().inject(this);

    setContentView(R.layout.activity_weather);

    if (savedInstanceState == null) {
      locationListFragment = new LocationListFragment();
      getSupportFragmentManager().beginTransaction()
          .add(R.id.content, locationListFragment)
          .commit();
    }
  }

  @Override public void onAddNewLocation() {
    startActivityForResult(new Intent(this, LocationPickerActivity.class), REQUEST_LOCATION);
  }

  @Override public void onLocationClicked(Location location) {
    WeatherDetailFragment fragment =
        (WeatherDetailFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_DETAIL);
    if (fragment != null) {
      // Dual pane
      fragment.setLocation(location);
    } else {
      // Single pane
      fragment = WeatherDetailFragment.newInstance(location);
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.content, fragment)
          .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
          .addToBackStack(null)
          .commit();
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_LOCATION && resultCode == RESULT_OK) {
      Location location = data.getParcelableExtra(EXTRA_LOCATION);
      service.saveLocation(location, new Callback<Location>() {
        @Override public void onResult(@Nullable Location result, @Nullable Throwable error) {
          if (error != null) {
            Log.e(TAG, "Error saving location: " + error.getLocalizedMessage(), error);
          } else {
            locationListFragment.updateLocations();
          }
        }
      });
      return;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
}
