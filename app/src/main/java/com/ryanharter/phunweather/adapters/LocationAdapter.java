package com.ryanharter.phunweather.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.ryanharter.phunweather.R;
import com.ryanharter.phunweather.sdk.common.model.Location;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

  public interface Listener {
    void onLocationClick(Location location);
  }

  private final List<Location> locations = new ArrayList<>();
  private final Listener listener;

  public LocationAdapter(Listener listener) {
    this.listener = listener;
  }

  public void clear() {
    locations.clear();
  }

  public void addAll(Collection<Location> locations) {
    this.locations.addAll(locations);
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_location, parent, false);
    return new ViewHolder(v);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    final Location l = locations.get(position);
    holder.title.setText(l.name());
    holder.subtitle.setText(l.zipcode());
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        listener.onLocationClick(l);
      }
    });
  }

  @Override public int getItemCount() {
    return locations.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.title) TextView title;
    @Bind(R.id.subtitle) TextView subtitle;
    @Bind(R.id.last_temp) TextView lastTemp;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

}
