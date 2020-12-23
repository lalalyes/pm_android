package com.fudan.project.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fudan.project.R;
import com.fudan.project.data.model.Activity;

import java.util.List;

import static com.fudan.project.ui.home.HomeFragment.ACIDFORDETAILS;
import static com.fudan.project.ui.home.HomeFragment.PICNAME;

public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.RecycleHolder> {

    private String TAG = "ItemViewAdapter";
    private Context mContext;

    public List<Activity> getDataList() {
        return dataList;
    }

    private List<Activity> dataList;


    public ItemViewAdapter(RecyclerView recyclerView, List dataList) {
        this.mContext = recyclerView.getContext();
        this.dataList = dataList;
    }

    public void setData(List<Activity> dataList) {
        if (null != dataList) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public RecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_list_item, parent, false);
        return new RecycleHolder((ViewGroup) view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleHolder holder, int position) {
        Activity activity = dataList.get(position);
        holder.titleView.setText(activity.getActivityName());
        holder.introductionView.setText(activity.getIntroduction());
        holder.typeView.setText("活动类型 ： "+activity.getType());
        String picUrl = "http://175.24.120.91/images/" + activity.getPicture();
        holder.imageView.setImageURL(picUrl);
        holder.itemView.setOnClickListener(getDetails(activity.getActivityId(),activity.getPicture()));
        holder.itemView.setOnHoverListener(hover(holder.itemView));
    }



    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView introductionView;
        TextView typeView;
        com.fudan.project.MyImageView imageView;

        public RecycleHolder(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.activity_list_item, parent, false));
            ;
            introductionView = itemView.findViewById(R.id.acIntroduction);
            titleView = itemView.findViewById(R.id.acTitle);
            typeView = itemView.findViewById(R.id.acType);
            imageView = itemView.findViewById(R.id.acCover);
        }
    }

    private View.OnHoverListener hover(final View view) {
        return new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                view.setBackgroundColor(Color.DKGRAY);
                return false;
            }
        };
    }


    private View.OnClickListener getDetails(final String ac_id, final String pic_name) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到相应的界面
                Intent intent = new Intent();
                intent.setClass(v.getContext(), DetailsActivity.class);

                intent.putExtra(PICNAME,pic_name);
                intent.putExtra(ACIDFORDETAILS, ac_id);

                v.getContext().startActivity(intent);
            }
        };
    }



}