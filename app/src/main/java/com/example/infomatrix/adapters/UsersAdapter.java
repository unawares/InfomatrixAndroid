package com.example.infomatrix.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.infomatrix.R;
import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserRealmObject;
import com.example.infomatrix.models.Users;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private Context context;
    private List<UserRealmObject> users;

    public UsersAdapter(Context context, List<UserRealmObject> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, container, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int position) {
        userViewHolder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView fullName;

        private TextView role;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.full_name);
            role = itemView.findViewById(R.id.role);
        }

        public void bind(UserRealmObject user) {
            fullName.setText(user.getFullName());
            role.setText(User.Role.get(user.getRole()).toDisplayString());
        }

    }

}
