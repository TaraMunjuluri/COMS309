package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Assuming Glide is used for image loading
import java.util.List;

/**
 * MatchAdapter binds a list of users to a RecyclerView for displaying potential matches.
 * It also provides functionality to handle match and reject actions through a listener interface.
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.UserViewHolder> {

    private List<User> users;
    private OnUserRequestClickListener listener;

    /**
     * Interface for handling user interactions on match and reject buttons.
     */
    public interface OnUserRequestClickListener {
        void onMatchClicked(User user);   // Called when the match button is clicked
        void onRejectClicked(User user); // Called when the reject button is clicked
    }

    /**
     * Constructor for MatchAdapter.
     *
     * @param users    The list of users to display.
     * @param listener The listener to handle match and reject actions.
     */
    public MatchAdapter(List<User> users, OnUserRequestClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    /**
     * ViewHolder class for displaying user information and handling interactions.
     */
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImage;
        public TextView profileName;
        public TextView profileMajor;
        public TextView profileClassification;
        public Button matchButton;
        public Button rejectButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            profileName = itemView.findViewById(R.id.profile_name);
            profileMajor = itemView.findViewById(R.id.profile_major);
            profileClassification = itemView.findViewById(R.id.profile_classification);
            matchButton = itemView.findViewById(R.id.match_button);
            rejectButton = itemView.findViewById(R.id.reject_button);
        }

        /**
         * Binds the user data to the views and sets click listeners for buttons.
         *
         * @param user     The user data to bind.
         * @param listener The listener to handle button actions.
         */
        public void bind(User user, OnUserRequestClickListener listener) {
            profileName.setText(user.getName()); // Replace with getName() if applicable
            profileMajor.setText(user.getMajor());
            profileClassification.setText(user.getClassification());

            // Load profile image if available
            if (user.getProfileImageUrl() != null) {
                Glide.with(profileImage.getContext())
                        .load(user.getProfileImageUrl())
                        .placeholder(R.drawable.user_profile) // Optional placeholder
                        .into(profileImage);
            } else {
                profileImage.setImageResource(R.drawable.user_profile); // Fallback image
            }

            // Set button click listeners
            if (listener != null) {
                matchButton.setOnClickListener(v -> listener.onMatchClicked(user));
                rejectButton.setOnClickListener(v -> listener.onRejectClicked(user));
            }
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

    /**
     * Updates the user list and refreshes the RecyclerView.
     *
     * @param newUsers The updated list of users.
     */
    public void updateUsers(List<User> newUsers) {
        this.users = newUsers;
        notifyDataSetChanged();
    }
}
