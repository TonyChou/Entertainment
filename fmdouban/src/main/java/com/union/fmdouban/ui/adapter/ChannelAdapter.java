package com.union.fmdouban.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.union.commonlib.ui.listener.ItemClickListener;
import com.union.fmdouban.R;
import com.union.fmdouban.bean.Channel;
import java.util.List;
/**
 * Created by zhouxiaming on 2016/3/14.
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelHolder> {
    private Context mContext;
    private List<Channel> mChannelList;
    private ItemClickListener listener;
    public ChannelAdapter(Context context, List<Channel> channels, ItemClickListener listener) {
        this.mContext = context;
        this.mChannelList = channels;
        this.listener = listener;
    }

    public void setData(List<Channel> channels) {
        this.mChannelList = channels;
    }
    @Override
    public ChannelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.channel_grid_item, null);
        return new ChannelHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mChannelList.size();
    }



    class ChannelHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView channelNameView;
        private ImageView channelImage;
        public ChannelHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            channelNameView = (TextView) itemView.findViewById(R.id.name);
            channelImage = (ImageView) itemView.findViewById(R.id.pic);
        }

        public void setData(int position) {
            Channel channel = mChannelList.get(position);
            channelNameView.setText(channel.getNameZh());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (listener != null) {
                listener.onItemClick(position);
            }
        }
    }

}
