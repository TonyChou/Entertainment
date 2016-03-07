package com.union.entertainment.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.union.entertainment.R;
import com.union.entertainment.module.picture.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipan on 14/12/26.
 */
public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.ListHolder>{


    String names[] = {"浏览器","输入法","健康","效率","教育","理财","阅读","个性化","购物","资讯","生活","工具","出行","通讯","拍照","社交","影音","安全","休闲","棋牌","益智","射击","体育","儿童","网游","角色","策略","经营","竞速"};
    Context context;
    private List<Photo> photoList = new ArrayList<Photo>();
    public PhotoGridAdapter(Context context){
        this.context = context;
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


    class ListHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        public ListHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.pic);
            name = (TextView) itemView.findViewById(R.id.name);
        }

        public void setData(int position){
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.example));
            name.setText(names[position % names.length]);
        }
    }
}
