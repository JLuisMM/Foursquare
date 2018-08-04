package com.example.luis.codingchallenge.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luis.codingchallenge.R;
import com.example.luis.codingchallenge.model.Photo;
import com.example.luis.codingchallenge.model.User;
import com.example.luis.codingchallenge.model.UserComment;
import com.example.luis.codingchallenge.utility.ViewUtility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.UserCommentViewHolder> {

    private ArrayList<UserComment> mUserComments;

    public UserCommentAdapter(ArrayList<UserComment> mUserComments) {
        this.mUserComments = mUserComments;
    }

    public void setUserComments(ArrayList<UserComment> mUserComments) {
        this.mUserComments = mUserComments;
    }

    @Override
    public UserCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_comment, parent, false);
        return new UserCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserCommentViewHolder holder, int position) {
        if (holder == null) {
            return;
        }

        UserComment comment = mUserComments.get(position);
        if (comment == null) {
            return;
        }

        holder.comment.setText(comment.getText());
        User user = comment.getUser();
        Photo photo = user != null ? user.getPhoto() : null;

        if (photo != null) {
            ViewUtility.loadProfileView(holder.userIcon, photo.getImageBySize("40x40"));
        }
    }

    @Override
    public int getItemCount() {
        if (mUserComments == null) {
            return 0;
        }
        return mUserComments.size();
    }

    static class UserCommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_user_comment_text)
        TextView comment;

        @BindView(R.id.card_user_comment_icon)
        ImageView userIcon;

        UserCommentViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
