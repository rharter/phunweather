package com.ryanharter.phunweather.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ryanharter.phunweather.PhunweatherApp;
import com.ryanharter.phunweather.R;
import com.ryanharter.phunweather.adapters.LocationAdapter;
import com.ryanharter.phunweather.sdk.Callback;
import com.ryanharter.phunweather.sdk.model.Location;
import com.ryanharter.phunweather.sdk.weather.WeatherService;
import java.util.List;
import javax.inject.Inject;


public class LocationListFragment extends Fragment implements LocationAdapter.Listener {

  public interface Listener {
    void onAddNewLocation();
    void onLocationClicked(Location location);
  }

  @Bind(R.id.empty) View empty;
  @Bind(R.id.loading) ProgressBar loading;
  @Bind(R.id.list) RecyclerView list;
  @Bind(R.id.add) FloatingActionButton button;

  @Inject WeatherService service;

  private LocationAdapter adapter;

  private static final Listener DEFAULT_LISTENER = new Listener() {
    @Override public void onAddNewLocation() { }
    @Override public void onLocationClicked(Location location) { }
  };
  private Listener listener = DEFAULT_LISTENER;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_location_list, container, false);
    ButterKnife.bind(this, v);
    return v;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ((PhunweatherApp) getActivity().getApplication()).component().inject(this);

    list.setLayoutManager(new LinearLayoutManager(getActivity()));
    list.setAdapter(adapter = new LocationAdapter(this));

    showLoading();
    service.getLocations(new Callback<List<Location>>() {
      @Override public void onResult(@Nullable List<Location> result, @Nullable Throwable error) {
        if (error != null) {
          onError(error);
        } else {
          onLocations(result);
        }
      }
    });
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (!(context instanceof Listener)) {
      throw new IllegalStateException(context.getClass().getSimpleName() + " must implement "
          + "LocationListFragment.Listener.");
    }
    listener = (Listener) context;
  }

  @Override public void onDetach() {
    listener = DEFAULT_LISTENER;
    super.onDetach();
  }

  private void showList() {
    list.setVisibility(View.VISIBLE);
    loading.setVisibility(View.GONE);
    empty.setVisibility(View.GONE);
  }

  private void showLoading() {
    list.setVisibility(View.GONE);
    loading.setVisibility(View.VISIBLE);
    empty.setVisibility(View.GONE);
  }

  private void showEmpty() {
    list.setVisibility(View.GONE);
    loading.setVisibility(View.GONE);
    empty.setVisibility(View.VISIBLE);
  }

  private void onLocations(List<Location> locations) {
    if (locations == null || locations.isEmpty()) {
      showEmpty();
      return;
    }

    adapter.clear();
    adapter.addAll(locations);
    showList();
  }

  private void onError(Throwable error) {
    // TODO
    showEmpty();
  }

  @Override public void onLocationClick(Location location) {
    listener.onLocationClicked(location);
  }

  @OnClick(R.id.add) public void onAddClick() {
    listener.onAddNewLocation();
  }
}
