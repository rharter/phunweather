package com.ryanharter.phunweather.sdk.internal;

import android.support.annotation.Nullable;
import com.ryanharter.phunweather.sdk.Callback;
import com.ryanharter.phunweather.sdk.model.Location;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import retrofit.Call;
import retrofit.Response;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class LocationServiceImplTest {

  ZipcodeApi api;
  Call<ZipcodeResult> call;

  LocationServiceImpl service;

  @SuppressWarnings("unchecked")
  @Before public void setup() {
    call = (Call<ZipcodeResult>) mock(Call.class);

    // the api will always return out mocked call object
    api = mock(ZipcodeApi.class);
    given(api.lookupZipcode(any(String.class))).willReturn(call);

    service = new LocationServiceImpl(api);
  }

  @Test public void passesNetworkFailureToCallback() {
    final ConnectException expected = new ConnectException();
    givenError(call, expected);

    TestCallback cb = new TestCallback();
    service.lookupZipcode("12345", cb);

    assertThat(cb.result).isNull();
    assertThat(cb.error).isEqualTo(expected);
  }

  @Test public void passesErrorOnNoPlacesFound() {
    final ZipcodeResult result = new ZipcodeResult("12345", "us", "us",
        new ArrayList<ZipcodeResult.Place>());
    givenResult(call, result);

    TestCallback cb = new TestCallback();
    service.lookupZipcode(result.zipcode, cb);

    assertThat(cb.result).isNull();
    assertThat(cb.error).isInstanceOf(IllegalArgumentException.class);
  }

  @Test public void returnsLocationMatchingFirstPlace() {
    Location expected = Location.builder()
        .name("Austin").zipcode("90210").latitude(1.0).longitude(2.0).build();
    List<ZipcodeResult.Place> places = Arrays.asList(
        new ZipcodeResult.Place(expected.name(),
            Double.toString(expected.latitude()), Double.toString(expected.longitude()),
            "Texas", "TX"),
        new ZipcodeResult.Place("Bolingbrook", "3.0", "4.0", "Illinois", "IL")
    );
    final ZipcodeResult result = new ZipcodeResult("90210", "USA", "us", places);
    givenResult(call, result);

    TestCallback cb = new TestCallback();
    service.lookupZipcode(result.zipcode, cb);

    assertThat(cb.error).isNull();
    assertThat(cb.result).isEqualTo(expected);
  }

  @SuppressWarnings("unchecked")
  private void givenResult(Call<ZipcodeResult> call, final ZipcodeResult result) {
    doAnswer(new Answer() {
      @Override public Object answer(InvocationOnMock invocation) throws Throwable {
        retrofit.Callback<ZipcodeResult> cb =
            (retrofit.Callback<ZipcodeResult>) invocation.getArguments()[0];
        cb.onResponse(Response.success(result), null);
        return null;
      }
    }).when(call).enqueue((retrofit.Callback<ZipcodeResult>) any(retrofit.Callback.class));
  }

  @SuppressWarnings("unchecked")
  private void givenError(Call<ZipcodeResult> call, final Throwable error) {
    doAnswer(new Answer() {
      @Override public Object answer(InvocationOnMock invocation) throws Throwable {
        retrofit.Callback<ZipcodeResult> cb = (retrofit.Callback<ZipcodeResult>) invocation.getArguments()[0];
        cb.onFailure(error);
        return null;
      }
    }).when(call).enqueue((retrofit.Callback<ZipcodeResult>) any(retrofit.Callback.class));
  }

  private static class TestCallback implements Callback<Location> {
    Location result;
    Throwable error;

    @Override public void onResult(@Nullable Location result, @Nullable Throwable error) {
      this.result = result;
      this.error = error;
    }
  }

}