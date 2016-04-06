package com.union.entertainment.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.union.commonlib.utils.DateUtil;
import com.union.commonlib.utils.UiUtils;
import com.union.entertainment.R;
import com.union.entertainment.module.picture.Photo;
import com.union.entertainment.ui.activity.LocationShowActivity;
import com.union.entertainment.ui.activity.PhotosViewPagerActivity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipan on 14/12/26.
 */
public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.ListHolder>{
    private int[] screen;
    Context context;

    private List<Photo> photoList = new ArrayList<Photo>();
    public PhotoGridAdapter(Activity context){
        this.context = context;
        screen = UiUtils.getScreenWidthAndHeight(context);
    }

    public void setData(List<Photo> data) {
        if (data != null && data.size() > 0) {
            synchronized (photoList) {
                photoList.clear();
                photoList.addAll(data);
                this.notifyDataSetChanged();
            }
        }
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);

        return new ListHolder(view);
    }


    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }


    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView icon;
        TextView name;

        public ListHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.pic);
            name = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        public void setData(int position){
            Photo photo = photoList.get(position);
            if (photo != null) {
                Picasso.with(context).load(new File(photo.getPath())).resize(screen[0] / 2, screen[0] / 2)
                        .centerCrop().config(Bitmap.Config.ARGB_8888).placeholder(R.drawable.example)
                        .into(icon);
                long date = photo.getModifyDate();
                String dateStr = DateUtil.string2unixTimestamp2(date);
                name.setText(dateStr);
            } else {
                icon.setImageDrawable(context.getResources().getDrawable(R.drawable.example));
                name.setText("");
            }



        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent intent = new Intent(context, PhotosViewPagerActivity.class);
            //Intent intent = new Intent(context, LocationShowActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("photos", (Serializable) photoList);
            bundle.putInt("position", position);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }
}
