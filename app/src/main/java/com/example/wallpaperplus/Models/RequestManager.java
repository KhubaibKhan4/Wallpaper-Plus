package com.example.wallpaperplus.Models;

import android.content.Context;
import android.widget.Toast;

import com.example.wallpaperplus.Listeners.OnFetchListener;
import com.example.wallpaperplus.Listeners.SearchListener;
import com.example.wallpaperplus.Models.CuratedApiResponse;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class RequestManager implements Serializable {
    Context context;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getCuratedWallpapers(OnFetchListener listener, String page) {
        CuratedApi curatedApi = retrofit.create(CuratedApi.class);
        Call<CuratedApiResponse> call = curatedApi.getWallpaper(page, "80");

        try {
            call.enqueue(new Callback<CuratedApiResponse>() {
                @Override
                public void onResponse(Call<CuratedApiResponse> call, Response<CuratedApiResponse> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    listener.OnFetch(response.body(), response.message());
                }

                @Override
                public void onFailure(Call<CuratedApiResponse> call, Throwable t) {
                    Toast.makeText(context, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void searchWallpaper(SearchListener searchListener, String query, String page) {
        SearchApi searchApi = retrofit.create(SearchApi.class);
        Call<SearchApiResponse> call = searchApi.getWallpaper(query, page, "80");
        try {
            call.enqueue(new Callback<SearchApiResponse>() {
                @Override
                public void onResponse(Call<SearchApiResponse> call, Response<SearchApiResponse> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    searchListener.onFetch(response.body(),response.message());
                }

                @Override
                public void onFailure(Call<SearchApiResponse> call, Throwable t) {
                    Toast.makeText(context, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public interface CuratedApi {
        @Headers({
                "Accept: application/json",
                "Authorization: Your Api Here"
        })
        @GET("curated")
        Call<CuratedApiResponse> getWallpaper(
                @Query("page") String page,
                @Query("per_page") String per_page
        );
    }


    public interface SearchApi {
        @Headers({
                "Accept: application/json",
                "Authorization: Your Api Here"
        })
        @GET("search")
        Call<SearchApiResponse> getWallpaper(
                @Query("query") String query,
                @Query("page") String page,
                @Query("per_page") String per_page
        );
    }
}
