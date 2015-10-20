package com.ryanharter.phunweather.activities;

import android.app.Activity;
import android.view.View;
import com.ryanharter.phunweather.BuildConfig;
import com.ryanharter.phunweather.TestPhunweatherApp;
import com.ryanharter.phunweather.inject.AppComponent;
import com.ryanharter.phunweather.inject.AppModule;
import com.ryanharter.phunweather.inject.DaggerAppComponent;
import com.ryanharter.phunweather.sdk.common.Callback;
import com.ryanharter.phunweather.sdk.common.model.Location;
import com.ryanharter.phunweather.sdk.location.LocationService;
import dagger.Module;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LocationPickerActivityTest {

  LocationService service;
  LocationPickerActivity activity;

  @Module public class TestModule extends AppModule {
    public TestModule() {
      super(RuntimeEnvironment.application);
    }

    @Override public LocationService provideLocationService() {
      return service;
    }
  }

  @Before public void setUp() {
    service = mock(LocationService.class);

    AppComponent component = DaggerAppComponent.builder().appModule(new TestModule()).build();
    ((TestPhunweatherApp) RuntimeEnvironment.application).setComponent(component);

    activity = Robolectric.setupActivity(LocationPickerActivity.class);
  }

  @Test public void testShowSearchContainer() {
    activity.showSearchContainer();
    assertThat(activity.searchContainer.getVisibility()).isEqualTo(View.VISIBLE);
    assertThat(activity.errorContainer.getVisibility()).isNotEqualTo(View.VISIBLE);
    assertThat(activity.loadingContainer.getVisibility()).isNotEqualTo(View.VISIBLE);
  }

  @Test public void testShowLoadingContainer() {
    activity.showLoadingContainer();
    assertThat(activity.searchContainer.getVisibility()).isNotEqualTo(View.VISIBLE);
    assertThat(activity.errorContainer.getVisibility()).isNotEqualTo(View.VISIBLE);
    assertThat(activity.loadingContainer.getVisibility()).isEqualTo(View.VISIBLE);
  }

  @Test public void testShowErrorContainer() {
    activity.showErrorContainer();
    assertThat(activity.searchContainer.getVisibility()).isNotEqualTo(View.VISIBLE);
    assertThat(activity.errorContainer.getVisibility()).isEqualTo(View.VISIBLE);
    assertThat(activity.loadingContainer.getVisibility()).isNotEqualTo(View.VISIBLE);
  }

  @Test public void clickingCancelCancelsActivity() {
    activity.onCancel();
    assertThat(shadowOf(activity).getResultCode()).isEqualTo(Activity.RESULT_CANCELED);
  }

  @Test public void incompleteZipCodeSetsError() {
    activity.zipcode.setText("123");
    activity.onSearch();
    assertThat(activity.searchContainer.getVisibility()).isEqualTo(View.VISIBLE);
    assertThat(activity.zipcode.getError().toString()).isNotEmpty();
  }

  @Test public void validZipCodeShowsLoadingView() {
    activity.zipcode.setText("12345");
    activity.onSearch();
    assertThat(activity.searchContainer.getVisibility()).isNotEqualTo(View.VISIBLE);
    assertThat(activity.loadingContainer.getVisibility()).isEqualTo(View.VISIBLE);
  }

  @Test public void unfoundZipShowError() {
    givenError(service, new IllegalArgumentException());

    activity.zipcode.setText("12345");
    activity.onSearch();

    assertThat(activity.searchContainer.getVisibility()).isNotEqualTo(View.VISIBLE);
    assertThat(activity.errorContainer.getVisibility()).isEqualTo(View.VISIBLE);
  }

  @Test public void validZipReturnsResult() {
    Location expected = Location.builder()
        .zipcode("60440").name("Bolingbrook").latitude(1.234).longitude(5.678)
        .build();
    givenResult(service, expected);

    activity.zipcode.setText("60440");
    activity.onSearch();

    assertThat(shadowOf(activity).getResultCode()).isEqualTo(Activity.RESULT_OK);

    Location actual = shadowOf(activity)
        .getResultIntent()
        .getParcelableExtra(LocationPickerActivity.EXTRA_LOCATION);
    assertThat(actual).isEqualTo(expected);
  }

  @SuppressWarnings("unchecked")
  private void givenResult(LocationService service, final Location result) {
    doAnswer(new Answer() {
      @Override public Object answer(InvocationOnMock invocation) throws Throwable {
        Callback<Location> cb = (Callback<Location>) invocation.getArguments()[1];
        cb.onResult(result, null);
        return null;
      }
    }).when(service).lookupZipcode(any(String.class), (Callback<Location>) any(Callback.class));
  }

  @SuppressWarnings("unchecked")
  private void givenError(LocationService service, final Throwable error) {
    doAnswer(new Answer() {
      @Override public Object answer(InvocationOnMock invocation) throws Throwable {
        Callback<Location> cb = (Callback<Location>) invocation.getArguments()[1];
        cb.onResult(null, error);
        return null;
      }
    }).when(service).lookupZipcode(any(String.class), (Callback<Location>) any(Callback.class));
  }
}