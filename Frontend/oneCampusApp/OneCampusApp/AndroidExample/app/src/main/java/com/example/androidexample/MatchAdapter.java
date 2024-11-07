package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.UserViewHolder> {

    private List<User> users;
    private OnUserRequestClickListener listener;

    public interface OnUserRequestClickListener {
        void onMatchClicked(User user);
        void onRejectClicked(User user);
    }

    public MatchAdapter(List<User> users, OnUserRequestClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImage;
        public TextView profileName;
        public TextView profileMajor;
        public TextView profileClassification;
        public Button matchButton;
        public Button rejectButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            profileName = itemView.findViewById(R.id.profile_name);
            profileMajor = itemView.findViewById(R.id.profile_major);
            profileClassification = itemView.findViewById(R.id.profile_classification);
            matchButton = itemView.findViewById(R.id.match_button);
            rejectButton = itemView.findViewById(R.id.reject_button);
        }

        public void bind(User user, OnUserRequestClickListener listener) {
            profileName.setText(user.getName());
            profileMajor.setText(user.getMajor());
            profileClassification.setText(user.getClassification());

            matchButton.setOnClickListener(v -> listener.onMatchClicked(user));
            rejectButton.setOnClickListener(v -> listener.onRejectClicked(user));
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_match, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(users.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
