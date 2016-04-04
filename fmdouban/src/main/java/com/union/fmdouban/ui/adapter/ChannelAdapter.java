package com.union.fmdouban.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.union.commonlib.ui.listener.ItemClickListener;
import com.union.fmdouban.R;
import com.union.fmdouban.api.bean.FMRichChannel;
import com.union.fmdouban.service.FMPlayerService;

import java.util.List;
/**
 * Created by zhouxiaming on 2016/3/14.
 */

public class ChannelAdapter extends BaseAdapter {
    private Context mContext;
    private List<FMRichChannel> mChannelList;
    private ItemClickListener listener;
    private Animation animation;
    private boolean isAnimationRun = false;
    public ChannelAdapter(Context context, List<FMRichChannel> channels, ItemClickListener listener) {
        this.mContext = context;
        this.mChannelList = channels;
        this.listener = listener;

        animation = AnimationUtils.loadAnimation(mContext, R.anim.cover_rotate_repeat_anim);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimationRun = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimationRun = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setData(List<FMRichChannel> channels) {
        this.mChannelList = channels;
    }

    public FMRichChannel getChannel(int position) {
        return mChannelList.get(position);
    }

    public List<FMRichChannel> getData() {
        return mChannelList;
    }


    @Override
    public int getCount() {
        return mChannelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mChannelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ChannelHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.channels_list_item, null);
            holder = new ChannelHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChannelHolder)convertView.getTag();
        }

        holder.setData(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
        return convertView;
    }


    class ChannelHolder{
        private View itemView;
        private TextView channelNameView;
        private RoundedImageView channelImage;
        public ChannelHolder(View itemView) {
            channelNameView = (TextView) itemView.findViewById(R.id.channel_name);
            channelImage = (RoundedImageView) itemView.findViewById(R.id.channel_cover);;
            this.itemView = itemView;
        }

        public void setData(int position) {
            FMRichChannel channel = mChannelList.get(position);
            channelNameView.setText(channel.getName());
            Picasso.with(mContext).load(channel.getBannerUrl()).config(Bitmap.Config.ARGB_8888).placeholder(R.drawable.example)
                    .into(channelImage);
            FMRichChannel currentPlayChannel = FMPlayerService.getCurrentChannel();
            if (currentPlayChannel != null && currentPlayChannel.getChannelId() == channel.getChannelId()
                    && FMPlayerService.isPlaying()) {
                runAnimation(channelImage);
            } else {
                cancelAnimation(channelImage);
            }
        }
    }

    private void runAnimation(ImageView image) {
        image.startAnimation(animation);
    }

    private void cancelAnimation(ImageView image) {
        image.clearAnimation();
    }



}
