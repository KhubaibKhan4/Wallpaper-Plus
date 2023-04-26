package com.example.wallpaperplus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wallpaperplus.Models.Photo;

import java.io.File;

public class DetailedActivity extends AppCompatActivity {
    Photo photo;
    ImageView imageView_wallpaper,backPressed;
    Button button_download, button_wallpaper;
    RelativeLayout relative_wallpaper;
    ProgressDialog dialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detailed);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Setting as Wallpaper...");


        photo = (Photo) getIntent().getSerializableExtra("photo");

        imageView_wallpaper = findViewById(R.id.imageView_wallpaper);
        button_download = findViewById(R.id.button_download);
        button_wallpaper = findViewById(R.id.button_wallpaper);
        relative_wallpaper = findViewById(R.id.relative_wallpaper);
        backPressed = (ImageView) findViewById(R.id.btnBackPress);

        relative_wallpaper.setBackgroundColor(Color.parseColor(photo.avg_color));

        Glide.with(this).load(photo.getSrc().getOriginal()).into(imageView_wallpaper);

        button_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager photosDownload = null;
                photosDownload = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

                Uri uri = Uri.parse(photo.getSrc().getLarge());

                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                        .setAllowedOverRoaming(false)
                        .setTitle("Wallpaper Download")
                        .setMimeType("image/jpeg")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + "wallpaper_download" + photo.getId() + ".jpg");


                photosDownload.enqueue(request);

                Toast.makeText(DetailedActivity.this, "Download Complete!", Toast.LENGTH_SHORT).show();
            }
        });

        button_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView_wallpaper.getDrawable() == null) {
                    Toast.makeText(DetailedActivity.this, "Please Wait Image is Loading...", Toast.LENGTH_SHORT).show();
                    return;
                }

                WallpaperManager wallpaperManager = WallpaperManager.getInstance(DetailedActivity.this);
                Bitmap bitmap = ((BitmapDrawable) imageView_wallpaper.getDrawable()).getBitmap();
                try {
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(DetailedActivity.this, "Wallpaper Applied", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(DetailedActivity.this, "Couldn't set wallpaper!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}