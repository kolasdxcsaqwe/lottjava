package com.example.tt.utils;

import com.example.tt.Bean.PostParamBean;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HttpRequest {

    static  HttpRequest httpRequest;
    static OkHttpClient client;

    public static HttpRequest getInstance()
    {

        if(httpRequest==null)
        {
            httpRequest=new HttpRequest();
        }

        if(client==null)
        {
            client=new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build();
        }
        return httpRequest;
    }


    public void post(String baseUrl, List<PostParamBean> params) {

        MyLog.e(baseUrl+"--->"+new Gson().toJson(params));

        FormBody.Builder builder=  new FormBody.Builder();
        for (PostParamBean postParamBean : params) {
            builder.add(postParamBean.getKey(), postParamBean.getValue());
        }
        RequestBody requestBody=builder.build();

        Request request=new Request.Builder().addHeader("content-type","application/json")
                .url(baseUrl).post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.e(baseUrl+"--->"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body()!=null)
                {
                    MyLog.l(baseUrl+"--->"+response.body().string());
                }

            }
        });
    }

    public void get(String baseUrl, List<PostParamBean> params,HttpCallBack httpCallBack) {

        StringBuilder sb=new StringBuilder();
        sb.append(baseUrl).append("?");
        for (PostParamBean postParamBean : params) {
          sb.append(postParamBean.getKey()).append("=").append(postParamBean.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length()-1);

        Request request=new Request.Builder().addHeader("content-type","application/json")
                .url(sb.toString()).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.e(baseUrl+"--->"+e.getMessage());
                if(httpCallBack!=null)
                {
                    httpCallBack.onError(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful() && response.body()!=null)
                {
                    String string=response.body().string();
                    MyLog.l(baseUrl+"--->"+string);
                    if(httpCallBack!=null)
                    {
                        httpCallBack.onSuccess(string);
                    }
                }

            }
        });
    }

}
