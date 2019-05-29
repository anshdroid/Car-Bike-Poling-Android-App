package com.example.app.saathiii;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.ViewHolder> {

    Context context;
    List<ReqRouteDetails> MainImageUploadInfoList;

    public RecyclerViewAdapter1(Context context, List<ReqRouteDetails> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items1, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ReqRouteDetails reqrouteDetails = MainImageUploadInfoList.get(position);

        holder.input_nname.setText(reqrouteDetails.getRName());
        holder.input_ddestination.setText(reqrouteDetails.getRDestination());
        holder.input_mmobile.setText(reqrouteDetails.getRMobile());
        holder.input_ttime.setText(reqrouteDetails.getRTime());
        holder.input_ddate.setText(reqrouteDetails.getRDate());
        holder.input_sseat.setText(reqrouteDetails.getRSeat());
        holder.input_ssource.setText(reqrouteDetails.getRSource());


        //Loading image from Glide library. *added for image*
        Glide.with(context).load(reqrouteDetails.getRimageURL()).apply(RequestOptions.circleCropTransform())
                .into(holder.input_iimage);

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView input_ddestination, input_mmobile, input_nname, input_ttime, input_ddate,input_sseat, input_ssource ;
        public  ImageView  input_iimage;

        public ViewHolder(View itemView) {

            super(itemView);

            input_nname = (TextView) itemView.findViewById(R.id.input_name);
            input_ddestination = (TextView) itemView.findViewById(R.id.input_destination);
            input_ssource = (TextView) itemView.findViewById(R.id.input_source);
            input_mmobile = (TextView) itemView.findViewById(R.id.input_mobile);
            input_ttime = (TextView) itemView.findViewById(R.id.input_time);
            input_ddate = (TextView) itemView.findViewById(R.id.input_date);
            input_sseat = (TextView) itemView.findViewById(R.id.input_seat);
            input_iimage= (ImageView)itemView.findViewById(R.id.imageView);
        }
    }
}