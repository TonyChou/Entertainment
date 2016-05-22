package com.union.fmdouban.api.data;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.union.fmdouban.api.ExecuteResult;
import com.union.fmdouban.api.FMApi;
import com.union.fmdouban.api.FMCallBack;
import com.union.fmdouban.api.FMParserFactory;
import com.union.fmdouban.api.bean.FMChannelType;
import com.union.fmdouban.api.bean.FMRichChannel;

import java.util.List;

/**
 * Created by zhouxiaming on 16/3/13.
 */
public class ChannelLoader extends AsyncTaskLoader<Boolean> implements FMCallBack {
    String TAG = "ChannelLoader";
    Context mContext;
    private int timeout = 30;
    ExecuteResult mResult;

    public ChannelLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        if (FMCache.getTypeList().size() < 1 || FMCache.getHotChannelsFromCache().size() < 1) {
            forceLoad();
        } else {
            deliverResult(true);
        }

    }

    @Override
    public Boolean loadInBackground() {
        sendRequest();
        waitForCompletion();
        return parserResult(mResult);
    }

    @Override
    public void deliverResult(Boolean result) {
        super.deliverResult(result);
    }

    private static boolean parserResult(ExecuteResult result) {
        List<FMChannelType> types = null;
        boolean parserFlag = false;
        if (result != null && result.getResult() == ExecuteResult.OK) {
            types = FMParserFactory.parserChannelTypeFromHtml(result.getResponseString());
            //解析热门频道放入缓存中
            List<FMRichChannel> channelList = FMParserFactory.parserChannelFromHtml(result.getResponseString());
            if (channelList != null && channelList.size() > 0) {
                FMCache.addHotChannelsToCache(channelList);
                parserFlag = true;
            }

            if (types != null && types.size() > 0) {
                FMCache.addTypeListToCache(types);
            } else {
                parserFlag = false;
            }
        }
        result = null;
        return parserFlag;
    }

    private void waitForCompletion() {
        long begin = System.currentTimeMillis();
        while (System.currentTimeMillis() - begin < timeout * 1000L) {
            if (mResult != null) {
                break;
            }
            SystemClock.sleep(100);
        }
    }

    public void sendRequest() {
        FMApi.getInstance().getHtmlContent(this);
    }

    @Override
    public void onRequestResult(ExecuteResult result) {
        this.mResult = result;
    }

    /**
     * 后台加载channel 数据
     */
    public static void loadChannelData() {
        FMApi.getInstance().getHtmlContent(new FMCallBack() {
            @Override
            public void onRequestResult(ExecuteResult result) {
                parserResult(result);
            }
        });
    }
}
