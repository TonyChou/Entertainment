package com.union.fmdouban.api;

import com.squareup.okhttp.Response;

/**
 * Created by zhouxiaming on 16/3/20.
 */
public class ExecuteResult {
    public static final int OK = 1;
    public static final int FAILED = 0;
    public static final int UNKNOWN = -1;
    private int result = -1; //0:失败；1成功; -1:request请求还未执行完毕
    private Response response;
    private String responseString;

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
