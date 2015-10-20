package com.ryanharter.phunweather.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.ryanharter.phunweather.PhunweatherApp;
import com.ryanharter.phunweather.R;
import com.ryanharter.phunweather.sdk.common.Callback;
import com.ryanharter.phunweather.sdk.common.model.Forecast;
import com.ryanharter.phunweather.sdk.common.model.Location;
import com.ryanharter.phunweather.sdk.weather.WeatherService;
import javax.inject.Inject;

public class WeatherDetailFragment extends Fragment {

  public static final String EXTRA_LOCATION = "location";
  private static final String TAG = WeatherDetailFragment.class.getSimpleName();

  @Bind(R.id.title) TextView title;
  @Bind(R.id.current_temp) TextView currentTemp;

  @Inject WeatherService service;

  public static WeatherDetailFragment newInstance(Location location) {
    WeatherDetailFragment f = new WeatherDetailFragment();

    Bundle args = new Bundle();
    if (location != null) {
      args.putParcelable(EXTRA_LOCATION, location);
    }
    f.setArguments(args);

    return f;
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_weather_detail, container, false);
    ButterKnife.bind(this, v);
    return v;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ((PhunweatherApp) getActivity().getApplication()).component().inject(this);

    if (savedInstanceState == null) {
      Location location = getArguments().getParcelable(EXTRA_LOCATION);
      if (location != null) {
        setLocation(location);
        return;
      }
    }
  }

  public void setLocation(@NonNull Location location) {
    title.setText(location.name());
    service.getForecast(location, new Callback<Forecast>() {
      @Override public void onResult(@Nullable Forecast result, @Nullable Throwable error) {
        if (error != null) {
          new AlertDialog.Builder(getActivity())
              .setTitle(R.string.error)
              .setMessage(R.string.error_forecast_retrieve)
              .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
                }
              })
              .show();
          Log.e(TAG, "Error retrieving forecast", error);
          return;
        }
        bindForecast(result);
      }
    });
  }

  public void bindForecast(Forecast forecast) {
    currentTemp.setText(
        getString(R.string.temp, String.valueOf(forecast.currently().temperature())));
  }
}
