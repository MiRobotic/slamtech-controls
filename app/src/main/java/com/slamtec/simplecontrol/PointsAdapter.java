package com.slamtec.simplecontrol;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.slamtec.simplecontrol.config.MapPoint;

import java.util.ArrayList;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.MyViewHolder> {

    private ArrayList<MapPoint> list;
    private OnPointClickListener pointClickListener;

    public PointsAdapter(ArrayList<MapPoint> list, OnPointClickListener pointClickListener) {
        this.list = list;
        this.pointClickListener = pointClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final MapPoint point = list.get(position);

        holder.tvName.setText(point.getName());
        final String pointDetails = "X: "+point.getX()+", Y: "+point.getY()+", Z: "+point.getZ();
        holder.tvPoint.setText(pointDetails);

        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pointClickListener.onDelete(point, holder.getAdapterPosition());
            }
        });

        holder.imgNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pointClickListener.onNavigate(point);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removePoint(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvPoint;
        ImageView imgNav, imgDel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvPoint = itemView.findViewById(R.id.tvPoint);
            imgNav = itemView.findViewById(R.id.imgNavigate);
            imgDel = itemView.findViewById(R.id.imgDelete);
        }
    }

    public interface OnPointClickListener{
        void onNavigate(MapPoint point);
        void onDelete(MapPoint point, int position);
    }

}
