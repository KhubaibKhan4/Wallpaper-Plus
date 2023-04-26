package com.example.wallpaperplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wallpaperplus.Adapters.CustomAdapter;
import com.example.wallpaperplus.Listeners.OnFetchListener;
import com.example.wallpaperplus.Listeners.OnSelectedListener;
import com.example.wallpaperplus.Listeners.SearchListener;
import com.example.wallpaperplus.Models.CuratedApiResponse;
import com.example.wallpaperplus.Models.Photo;
import com.example.wallpaperplus.Models.RequestManager;
import com.example.wallpaperplus.Models.SearchApiResponse;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSelectedListener {

    RecyclerView recyclerView;
    RequestManager requestManager;
    CustomAdapter adapter;
    SearchView searchView;
    int search_page;
    ProgressDialog dialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Please Wait...");
        dialog.setMessage("Wallpapers are Being Load from Server.\n Please Be patience...");
        dialog.show();

        requestManager = new RequestManager(this);
        requestManager.getCuratedWallpapers(listener, "1");

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                requestManager.searchWallpaper(search_listener, query, "1");
                dialog.setTitle("Your Wallpaper is Being Loaded..." + query);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private final SearchListener search_listener = new SearchListener() {
        @Override
        public void onFetch(SearchApiResponse response, String message) {
            dialog.dismiss();
            if (response.getPhotos().isEmpty()) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "No Image Found!!", Toast.LENGTH_SHORT).show();
                return;
            }
            search_page = response.getPage();
            showData(response.getPhotos());
            dialog.dismiss();
        }

        @Override
        public void OnError(String message) {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };


    private final OnFetchListener listener = new OnFetchListener() {
        @Override
        public void OnFetch(CuratedApiResponse response, String message) {
            showData(response.getPhotos());
            dialog.dismiss();
        }

        @Override
        public void OnError(String message) {
            dialog.dismiss();
        }
    };


    private void showData(List<Photo> photos) {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        adapter = new CustomAdapter(this, photos, this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onClick(Photo photo) {
        startActivity(new Intent(MainActivity.this, DetailedActivity.class)
                .putExtra("photo", photo));
    }


}