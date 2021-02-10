package com.example.emenu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.StackView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emenu.R;
import com.example.emenu.pojos.Post;

import java.util.List;

public class PostsAdapter extends
        RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, idTextView, bodyTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleText);
            idTextView = itemView.findViewById(R.id.idText);
            bodyTextView = itemView.findViewById(R.id.bodyText);
        }
    }

    private List<Post> postList;

    // Pass in the contact array into the constructor
    public PostsAdapter(List<Post> posts) {
        postList = posts;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.post_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        Post post = postList.get(position);

        // Set item views based on your views and data model
        holder.titleTextView.setText(post.getTitle());
        holder.idTextView.setText(String.valueOf(post.getId()));
        holder.bodyTextView.setText(post.getBody());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
