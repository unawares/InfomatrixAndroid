package com.example.infomatrix.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.infomatrix.R;
import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserRealmObject;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.BaseViewHolder> {

    private static final int OTHER_ITEMS_COUNT = 3;
    private static final int HEADER = 0;
    private static final int SEARCH = 1;
    private static final int USER_ITEM_HEADER = 2;
    private static final int ITEM = 3;

    private Context context;
    private List<UserRealmObject> users;
    private List<UserRealmObject> filtered;

    private void filter(String search) {
        filtered.clear();
        for (UserRealmObject userRealmObject : users) {
            if (userRealmObject.getFullName().toLowerCase().contains(search.toLowerCase())) {
                filtered.add(userRealmObject);
            }
        }
        notifyDataSetChanged();
    }

    public UsersAdapter(Context context, List<UserRealmObject> users) {
        this.context = context;
        this.filtered = new ArrayList<>();
        this.users = users;
        filter("");
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup container, int type) {
        View view = null;
        switch (type) {
            case HEADER:
                view = LayoutInflater.from(context).inflate(R.layout.header_item, container, false);
                return new HeaderViewHolder(view);
            case SEARCH:
                view = LayoutInflater.from(context).inflate(R.layout.search_item, container, false);
                return new SearchViewHolder(view);
            case USER_ITEM_HEADER:
                view = LayoutInflater.from(context).inflate(R.layout.user_item_header, container, false);
                return new UserItemHeaderViewHolder(view);
            case ITEM:
            default:
                view = LayoutInflater.from(context).inflate(R.layout.user_item, container, false);
                return new UserViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return HEADER;
            case 1:
                return SEARCH;
            case 2:
                return USER_ITEM_HEADER;
            default:
                return ITEM;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int position) {
        if (baseViewHolder instanceof UserViewHolder) {
            ((UserViewHolder) baseViewHolder).bind(filtered.get(position - OTHER_ITEMS_COUNT));
        }

    }

    @Override
    public int getItemCount() {
        return filtered.size() + OTHER_ITEMS_COUNT;
    }

    abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        private BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    class HeaderViewHolder extends BaseViewHolder {

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class SearchViewHolder extends BaseViewHolder {

        private EditText editText;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.search_edit_text);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    filter(s.toString());
                }

            });
        }

    }

    class UserItemHeaderViewHolder extends BaseViewHolder {

        private UserItemHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    class UserViewHolder extends BaseViewHolder {

        private TextView fullName;
        private TextView role;
        private RadioButton isFoodRadioButton;
        private RadioButton isTransportRadioButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.full_name);
            role = itemView.findViewById(R.id.role);
            isFoodRadioButton = itemView.findViewById(R.id.is_food_radio_button);
            isTransportRadioButton = itemView.findViewById(R.id.is_transport_radio_button);
        }

        public void bind(UserRealmObject user) {
            fullName.setText(user.getFullName());
            role.setText(User.Role.get(user.getRole()).toDisplayString());
            isFoodRadioButton.setChecked(user.isFood());
            isTransportRadioButton.setChecked(user.isTransport());
        }

    }

}
