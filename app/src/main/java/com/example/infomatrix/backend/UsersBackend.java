package com.example.infomatrix.backend;

import com.example.infomatrix.models2.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersBackend {

    private static UsersBackend instance;

    private Map<String, User> userMap;

    private UsersBackend() {
        userMap = new HashMap<>();
    }

    public static UsersBackend getInstance() {
        if (instance ==null) {
            instance = new UsersBackend();
        }
        return instance;
    }

    public void setUsers(List<User> users) {
        userMap.clear();
        for (User user : users) {
            userMap.put(user.getCode(), user);
        }
    }

    public User getUser(String key) {
        return userMap.get(key);
    }

}
