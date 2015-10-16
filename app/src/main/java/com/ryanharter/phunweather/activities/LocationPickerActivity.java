package com.ryanharter.phunweather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ryanharter.phunweather.PhunweatherApp;
import com.ryanharter.phunweather.R;
import com.ryanharter.phunweather.sdk.Callback;
import com.ryanharter.phunweather.sdk.LocationService;
import com.ryanharter.phunweather.sdk.model.Location;
import javax.inject.Inject;

/**
 * Presents a dialog that the user can use to choose a location.
 * <p>
 * The selected location will be returned to <code>Activity#onActivityResult()</code>
 */
public class LocationPickerActivity extends AppCompatActivity {

  public static final String EXTRA_LOCATION = "location";

  @Bind(R.id.search_container) RelativeLayout searchContainer;
  @Bind(R.id.error_container) RelativeLayout errorContainer;
  @Bind(R.id.loading) ProgressBar loadingContainer;
  @Bind(R.id.zipcode) EditText zipcode;

  @Inject LocationService service;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_location);
    ButterKnife.bind(this);
    ((PhunweatherApp) getApplication()).component().inject(this);

    showSearchContainer();
  }

  void showSearchContainer() {
    searchContainer.setVisibility(View.VISIBLE);
    errorContainer.setVisibility(View.INVISIBLE);
    loadingContainer.setVisibility(View.GONE);
  }

  void showLoadingContainer() {
    searchContainer.setVisibility(View.INVISIBLE);
    errorContainer.setVisibility(View.INVISIBLE);
    loadingContainer.setVisibility(View.VISIBLE);
  }

  void showErrorContainer() {
    searchContainer.setVisibility(View.INVISIBLE);
    errorContainer.setVisibility(View.VISIBLE);
    loadingContainer.setVisibility(View.GONE);
  }

  @OnClick(R.id.search) public void onSearch() {
    String zip = zipcode.getText().toString();
    if (zip.length() != 5) {
      zipcode.setError(getString(R.string.error_incomplete_zip));
      return;
    }
    showLoadingContainer();
    service.lookupZipcode(zip, new Callback<Location>() {
      @Override public void onResult(@Nullable Location result, @Nullable Throwable error) {
        if (error != null) {
          onLookupError(error);
        } else {
          onLocationReceived(result);
        }
      }
    });
  }

  private void onLocationReceived(Location result) {
    Intent data = new Intent();
    data.putExtra(EXTRA_LOCATION, result);
    setResult(RESULT_OK, data);
    finish();
  }

  private void onLookupError(Throwable error) {
    showErrorContainer();
  }

  @OnClick(R.id.error_try_again) public void onTryAgain() {
    showSearchContainer();
  }

  @OnClick({R.id.cancel, R.id.error_cancel}) public void onCancel() {
    setResult(RESULT_CANCELED);
    finish();
  }
}
