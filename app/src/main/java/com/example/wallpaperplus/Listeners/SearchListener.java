package com.example.wallpaperplus.Listeners;

import com.example.wallpaperplus.Models.SearchApiResponse;

public interface SearchListener {
    void onFetch(SearchApiResponse response, String message);

    void OnError(String message);
}
