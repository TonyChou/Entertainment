package com.union.fmdouban.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liblistview.adapter.BaseFacePreloadExpandableListAdapter;
import com.liblistview.widget.AbsListView;
import com.liblistview.widget.ExpandableListView;
import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tulips.douban.model.ChannelsPage;
import com.union.fmdouban.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 2017/4/13.
 */

public class ChannelGroupAdapter extends BaseFacePreloadExpandableListAdapter implements View.OnClickListener, View.OnLongClickListener{
    private static final String TAG = "ChannelGroupAdapter";

    private Context mContext;
    private OnChannelClickListener onChannelClickListener;
    private static final int CHILD_VIEW_TYPE_COUNT = 1;
    private static final int CHILD_VIEW_TYPE_BUDDY = 0;
    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    private AbsListView.OnScrollListener mOnScrollListener;
    public List<ChannelsPage.Groups> mChannelGroupList = new ArrayList<>();

    public ChannelGroupAdapter(Context context, ExpandableListView lv, OnChannelClickListener onClick) {
        super(context, lv);
        mContext = context;
        onChannelClickListener = onClick;
        mAttachedView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //点击Group view 不收起
                return true;
            }
        });
    }

    public void setGroupList(List<ChannelsPage.Groups> groups) {
        this.mChannelGroupList = groups;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mScrollState = scrollState;
        super.onScrollStateChanged(view, scrollState);
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // TODO Do Sth.
        }
        if (null != mOnScrollListener) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        if (null != mOnScrollListener) {
            mOnScrollListener.onScroll(view, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChannelGroupList.get(groupPosition).channels.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return super.getChildId(groupPosition, childPosition);
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return CHILD_VIEW_TYPE_BUDDY;
    }

    @Override
    public int getChildTypeCount() {
        return CHILD_VIEW_TYPE_COUNT;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChannelGroupAdapter.ChannelHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.channels_list_item, null);
            holder = new ChannelGroupAdapter.ChannelHolder(convertView);
            convertView.setTag(holder);
            convertView.setOnClickListener(this);
        } else {
            holder = (ChannelGroupAdapter.ChannelHolder)convertView.getTag();
        }


        holder.setData((ChannelsPage.Channel) getChild(groupPosition, childPosition));
        holder.channelPosition = childPosition;
        holder.channel = (ChannelsPage.Channel)getChild(groupPosition, childPosition);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChannelGroupList.get(groupPosition).channels.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mChannelGroupList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mChannelGroupList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mChannelGroupList.get(groupPosition).groupId;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        GroupHolder holder = null;
        if (convertView != null) {
            holder = (GroupHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.channel_group_title, parent, false);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
            //convertView.setOnClickListener(this);
        }

        holder.setData((ChannelsPage.Groups)getGroup(groupPosition));
        holder.groupPosition = groupPosition;
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for(int i = 0; i < getGroupCount(); i++) {
            mAttachedView.expandGroup(i);
        }
    }

    @Override
    public int getHeaderViewLayoutResourceId() {
        return R.layout.channel_group_title;
    }

    @Override
    public void configHeaderView(View header, int groupPosition) {
        GroupHolder holder = (GroupHolder) header.getTag();
        if(holder == null) {
            holder = new GroupHolder(header);
            header.setTag(holder);
        }
        holder.setData((ChannelsPage.Groups)getGroup(groupPosition));
    }


    @Override
    public boolean onLongClick(final View v) {
        Log.i(TAG, "onLongClick " + v.getId());
        return true;
    }

    @Override
    public void onClick(View v) {
        Object obj = v.getTag();
        if (obj instanceof GroupHolder) {
            GroupHolder holder = (GroupHolder) obj;
            if (mAttachedView.isGroupExpanded(holder.groupPosition)) {
                mAttachedView.collapseGroup(holder.groupPosition);
            } else {
                mAttachedView.expandGroup(holder.groupPosition);
            }
        } else if (obj instanceof ChannelHolder) {
            ChannelHolder holder = (ChannelHolder) obj;
            if (onChannelClickListener != null) {
                onChannelClickListener.onChannelClick(holder.channel);
            }
        }
    }


    class ChannelHolder{
        public int channelPosition;
        private ChannelsPage.Channel channel;
        private TextView channelNameView;
        private RoundedImageView channelImage;
        public ChannelHolder(View itemView) {
            channelNameView = (TextView) itemView.findViewById(R.id.channel_name);
            channelImage = (RoundedImageView) itemView.findViewById(R.id.channel_cover);;
        }

        public void setData(ChannelsPage.Channel channel) {
            channelNameView.setText(channel.channelName);
            Picasso.with(mContext).load(channel.cover).config(Bitmap.Config.ARGB_8888).placeholder(R.drawable.example)
                    .into(channelImage);
        }
    }

    class GroupHolder {
        public int groupPosition;
        private TextView titleView;
        public GroupHolder(View itemView) {
            titleView = (TextView) itemView.findViewById(R.id.group_name);
        }

        public void setData(ChannelsPage.Groups group) {
            if (TextUtils.isEmpty(group.groupName)) {
                titleView.setText(mContext.getString(R.string.douban_channels_group_null));
            } else {
                titleView.setText(group.groupName);
            }
        }
    }

    public interface OnChannelClickListener {
        public void onChannelClick(ChannelsPage.Channel channel);
    }
}
