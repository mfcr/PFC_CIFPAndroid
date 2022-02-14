package com.example.pfc.remoto;

import com.example.pfc.app.Constants;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader(Constants.API_KEY_NAME,Constants.API_KEY_VALUE)
                .build();
        Response response = chain.proceed(request);
        return response;
    }
}
