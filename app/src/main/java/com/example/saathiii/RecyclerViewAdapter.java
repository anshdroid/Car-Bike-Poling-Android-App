package com.example.app.saathiii;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<RouteDetails> MainImageUploadInfoList;

    public RecyclerViewAdapter(Context context, List<RouteDetails> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RouteDetails routeDetails = MainImageUploadInfoList.get(position);

        holder.input_ssource.setText(routeDetails.getSource());
        holder.input_ddestination.setText(routeDetails.getDestination());
        holder.input_mmobile.setText(routeDetails.getMobile());
        holder.input_vvehical.setText(routeDetails.getVehical());
        holder.input_ffare.setText(routeDetails.getFare());
        holder.input_sseat.setText(routeDetails.getSeat());
        holder.input_ttime.setText(routeDetails.getTime());
        holder.input_ddate.setText(routeDetails.getDate());
        holder.input_nname.setText(routeDetails.getName());

        //Loading image from Glide library. *added for image*
        Glide.with(context).load(routeDetails.getimageURL()).apply(RequestOptions.circleCropTransform())
               .into(holder.input_iimage);

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView input_ssource;
        public TextView input_ddestination, input_mmobile, input_vvehical, input_ffare, input_ttime, input_ddate, input_sseat, input_nname;
        public ImageView input_iimage;
        public ViewHolder(View itemView) {

            super(itemView);

            input_ssource = (TextView) itemView.findViewById(R.id.input_source);
            input_ddestination = (TextView) itemView.findViewById(R.id.input_destination);
            input_mmobile = (TextView) itemView.findViewById(R.id.input_mobile);
            input_vvehical = (TextView) itemView.findViewById(R.id.input_vehical);
            input_ffare = (TextView) itemView.findViewById(R.id.input_fare);
            input_sseat = (TextView) itemView.findViewById(R.id.input_seat);
            input_ttime = (TextView) itemView.findViewById(R.id.input_time);
            input_ddate = (TextView) itemView.findViewById(R.id.input_date);
            input_nname = (TextView) itemView.findViewById(R.id.input_name);
            input_iimage= (ImageView)itemView.findViewById(R.id.imageView);
        }
    }
}