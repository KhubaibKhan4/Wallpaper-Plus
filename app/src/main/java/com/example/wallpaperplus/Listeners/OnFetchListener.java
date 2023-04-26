package com.example.wallpaperplus.Listeners;

import com.example.wallpaperplus.Models.CuratedApiResponse;


import java.util.List;

public interface OnFetchListener {
    void OnFetch(CuratedApiResponse response, String message);

    void OnError(String message);
}
