package com.union.fmximalaya.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.squareup.picasso.Picasso;
import com.union.fmximalaya.R;
import com.union.fmximalaya.bean.ItemData;

/**
 * Created by zhouxiaming on 2016/3/23.
 */

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ChannelViewHolder> {
    private final int ITEM_TYPE_1 = 0;
    private final int ITEM_TYPE_2 = 1;
    private final int ITEM_TYPE_3 = 2;
    private Context mContext;
    private List<ItemData> mItemList;
    public ChannelListAdapter(Context context, List<ItemData> itemDataList) {
        this.mContext = context;
        this.mItemList = itemDataList;
    }

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position).getType();
    }


    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ChannelViewHolder viewHolder = null;
        View itemView = null;
        if (viewType == ITEM_TYPE_1) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.channel_item_one, null);
        } else if (viewType == ITEM_TYPE_2) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.channel_item_two, null);
        } else if (viewType == ITEM_TYPE_3) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.channel_item_three, null);
        }

        viewHolder = new ChannelViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return this.mItemList.size();
    }

    class TypeOneHolder extends ChannelViewHolder {

        public TypeOneHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(int position) {
            super.setData(position);
        }
    }


    class ChannelViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private ImageView mImageView;
        public ChannelViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.name);
            mImageView = (ImageView)itemView.findViewById(R.id.cover);
        }

        public void setData(int position) {
            ItemData data = mItemList.get(position);
            mTextView.setText(data.getName() + "    " + data.getType());
            Picasso.with(mContext).load(data.getImageUrl()).resize(200, 200)
                    .centerCrop().config(Bitmap.Config.ARGB_8888).placeholder(R.drawable.example)
                    .into(mImageView);
        }
    }
}
