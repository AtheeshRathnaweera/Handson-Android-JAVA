package com.example.emenu.api;

import com.example.emenu.pojos.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIInterface {

    @GET("/posts/{id}")
    Call<Post> getPostById(@Path("id") int id);

    @GET("/posts")
    Call<List<Post>> getAllPosts();

    @POST("/posts")
    Call<Post> createPost(@Body Post post);

    @PUT("/posts/{id}")
    Call<Post> createPost(@Path("id") int id, @Body Post post);

    @DELETE("/posts/{id}")
    Call<Boolean> deleteAPost(@Path("id") int id);
}
