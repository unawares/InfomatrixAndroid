package com.example.infomatrix.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.infomatrix.R;
import com.example.infomatrix.database.DBManager;
import com.example.infomatrix.models.FilterItem;
import com.example.infomatrix.models.User;
import com.example.infomatrix.models.UserRealmObject;
import com.example.infomatrix.utils.Callback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.BaseViewHolder> {

    private static final int OTHER_ITEMS_COUNT = 4;
    private static final int HEADER = 0;
    private static final int SEARCH = 1;
    private static final int FILTER = 2;
    private static final int USER_ITEM_HEADER = 3;
    private static final int ITEM = 4;

    private Context context;
    private List<UserRealmObject> users;
    private List<UserRealmObject> filtered;
    private Set<Integer> hiddenRoles;
    private String lastFilterSearch;
    private SearchViewHolder searchViewHolder;

    private OnFilterClickListener onFilterClickListener;

    private boolean isFiltered;

    private void syncHiddenRoles() {
        hiddenRoles.clear();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        for (FilterItem filterItem : FilterItem.build()) {
            if (!sharedPreferences.getBoolean(filterItem.getKey(), true)) {
                hiddenRoles.add(filterItem.getRole().getIdentifier());
            }
        }
    }

    private void filter(String search) {
        filtered.clear();
        for (UserRealmObject userRealmObject : users) {
            if (userRealmObject.getFullName().toLowerCase().contains(search.toLowerCase()) && !hiddenRoles.contains(userRealmObject.getRole())) {
                filtered.add(userRealmObject);
            }
        }
        isFiltered = !search.isEmpty() || !hiddenRoles.isEmpty();
        lastFilterSearch = search;
        notifyDataSetChanged();
    }

    public UsersAdapter(Context context, List<UserRealmObject> userRealmObjects, OnFilterClickListener onFilterClickListener) {
        this.context = context;
        this.users = userRealmObjects;
        this.filtered = new ArrayList<>();
        this.onFilterClickListener = onFilterClickListener;
        this.hiddenRoles = new HashSet<>();
        this.isFiltered = false;
        this.lastFilterSearch = "";
        syncHiddenRoles();
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
            case FILTER:
                view = LayoutInflater.from(context).inflate(R.layout.filter_item, container, false);
                return new FilterViewHolder(view);
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
                return FILTER;
            case 3:
                return USER_ITEM_HEADER;
            default:
                return ITEM;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                UserRealmObject userRealmObject = filtered.get(position - OTHER_ITEMS_COUNT);
                if (userRealmObject.isValid()) {
                    ((UserViewHolder) baseViewHolder).bind(userRealmObject);
                } else {
                    ((UserViewHolder) baseViewHolder).bind(null);
                }
                ((UserViewHolder) baseViewHolder).index.setText(Integer.toString(position - OTHER_ITEMS_COUNT + 1));
                break;
            case FILTER:
                ((FilterViewHolder) baseViewHolder).counter.setText(Integer.toString(filtered.size()));
                ((FilterViewHolder) baseViewHolder).label.setText(isFiltered ? "Filtered users:" : "Users:");
                break;
            case HEADER:
                ((HeaderViewHolder) baseViewHolder).headerTextView.setText("Users");
                break;
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

    public void setUsers(List<UserRealmObject> users) {
        this.users = users;
        syncHiddenRoles();
        filter(lastFilterSearch);
    }

    class HeaderViewHolder extends BaseViewHolder {

        private TextView headerTextView;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.header_text_view);
        }
    }

    class SearchViewHolder extends BaseViewHolder {

        private EditText editText;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            searchViewHolder = this;
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
                    filter(s.toString().trim());
                }

            });
        }

    }

    class FilterViewHolder extends BaseViewHolder {

        private TextView label;
        private TextView counter;
        private Button filter;

        private FilterViewHolder(@NonNull View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.counter_label);
            counter = itemView.findViewById(R.id.counter);
            filter = itemView.findViewById(R.id.filter);
            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onFilterClickListener != null) {
                        if (searchViewHolder != null) {
                            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(searchViewHolder.editText.getWindowToken(), 0);
                            searchViewHolder.editText.setFocusable(false);
                        }
                        onFilterClickListener.onFilterClick(new Callback() {
                            @Override
                            public void call() {
                                searchViewHolder.editText.setFocusableInTouchMode(true);
                                syncHiddenRoles();
                                filter(lastFilterSearch);
                            }
                        });
                    }
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

        private TextView index;
        private TextView fullName;
        private TextView role;
        private RadioButton isFoodRadioButton;
        private RadioButton isTransportRadioButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            index = itemView.findViewById(R.id.index);
            fullName = itemView.findViewById(R.id.full_name);
            role = itemView.findViewById(R.id.role);
            isFoodRadioButton = itemView.findViewById(R.id.is_food_radio_button);
            isTransportRadioButton = itemView.findViewById(R.id.is_transport_radio_button);
        }

        public void bind(UserRealmObject user) {
            if (user != null) {
                fullName.setText(user.getFullName());
                role.setText(User.Role.get(user.getRole()).toDisplayString());
                isFoodRadioButton.setChecked(user.isFood());
                isTransportRadioButton.setChecked(user.isTransport());
            } else {
                index.setTextColor(Color.RED);
                fullName.setTextColor(Color.RED);
                role.setTextColor(Color.RED);
                fullName.setText("Deleted User");
                role.setText("Invalid");
                isFoodRadioButton.setChecked(false);
                isTransportRadioButton.setChecked(false);
            }
        }

    }

    public interface OnFilterClickListener {

        void onFilterClick(Callback callback);

    }

}
