package com.example.emenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.emenu.adapters.PostsAdapter;
import com.example.emenu.api.APIInterface;
import com.example.emenu.pojos.Post;
import com.example.emenu.utils.APIClient;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    APIInterface apiInterface;
    int MY_PERMISSIONS_REQUEST_INTERNET = 1;
    boolean ACTIVE_INTERNET_CONNECTION = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("All Posts");
        }

        checkPermissions();

        if (ACTIVE_INTERNET_CONNECTION) {
            getAllPostsAndShow();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Need internet connection for continue. Please turn on the internet. " +
                    "Please restart the app after the you turned on the internet.")
                    .setCancelable(false)
                    .setPositiveButton("Connect to WIFI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            finish();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            System.exit(0);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(getApplicationContext(), "Need internet access to continue this action", Toast.LENGTH_LONG).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE}, MY_PERMISSIONS_REQUEST_INTERNET);

                checkInternetConnection();
            }
        } else {
            Log.i("app-log", "already have the permission");
            MY_PERMISSIONS_REQUEST_INTERNET = 1;
            //permission has granted
            checkInternetConnection();
        }

    }

    private void checkInternetConnection() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());

            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    ACTIVE_INTERNET_CONNECTION = true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    ACTIVE_INTERNET_CONNECTION = true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    ACTIVE_INTERNET_CONNECTION = true;
                }
            }

        }
    }

    private void getAllPostsAndShow() {
        //instantiate API client
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<List<Post>> allPostsCall = apiInterface.getAllPosts();
        allPostsCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d("app-log", response.code() + " "+response.body().size());
                List<Post> recPostList = response.body();

                RecyclerView rvPosts = findViewById(R.id.rvPosts);
                PostsAdapter postsAdapter = new PostsAdapter(recPostList);
                rvPosts.setAdapter(postsAdapter);
                rvPosts.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("app-log", "all post call failed " + t.getLocalizedMessage());
                call.cancel();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("app-log","on resume started");
    }
}
