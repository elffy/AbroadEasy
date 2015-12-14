package com.original.abroadeasy.network;

import com.original.abroadeasy.model.ProgramItem;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

public interface RetrofitService {
    @GET("/data/{startNum}")
    List<ProgramItem> getProgramList(@Path("startNum") int num);
}
