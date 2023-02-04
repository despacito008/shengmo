package com.aiwujie.shengmo.tim.location;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.amap.api.services.geocoder.GeocodeAddress;

import java.util.List;

public class LocationAddressAdapter extends RecyclerView.Adapter<LocationAddressAdapter.AddressHolder>{

    Context context;
    List<GeocodeAddress> geocodeAddressList;

    public LocationAddressAdapter(Context context, List<GeocodeAddress> geocodeAddressList) {
        this.context = context;
        this.geocodeAddressList = geocodeAddressList;
    }

    @Override
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location_address,parent,false);
        return new AddressHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return geocodeAddressList.size();
    }

    class AddressHolder extends RecyclerView.ViewHolder {
        TextView tvItem;
        public AddressHolder(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_item_location_address);
        }

        public void setData(int position) {
            tvItem.setText(geocodeAddressList.get(position).getFormatAddress());
        }
    }
}
