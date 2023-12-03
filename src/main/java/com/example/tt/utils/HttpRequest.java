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
            client=new OkHttpClient();
        }
        return httpRequest;
    }


    public void post(String baseUrl, List<PostParamBean> params) {

        MyLog.e(baseUrl+"--->"+new Gson().toJson(params));
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

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

}
