package com.example.infomatrix.models;

import java.util.ArrayList;
import java.util.List;

public class FilterItem {

    private String key;
    private String label;
    private boolean isChecked;
    private User.Role role;

    public static List<FilterItem> build() {
        List<FilterItem> results = new ArrayList<>();
        results.add(new FilterItem("is_admin_shown", User.Role.ADMIN, "Show Admins", true));
        results.add(new FilterItem("is_volunteer_shown", User.Role.VOLUNTEER, "Show Volunteers", true));
        results.add(new FilterItem("is_supervisor_shown", User.Role.SUPERVISOR, "Show Supervisors", true));
        results.add(new FilterItem("is_student_shown", User.Role.STUDENT, "Show Students", true));
        results.add(new FilterItem("is_guest_shown", User.Role.GUEST, "Show Guests", true));
        return results;
    }

    public FilterItem() {
        label = "";
        isChecked = false;
    }

    public FilterItem(String key, User.Role role, String label, boolean isChecked) {
        this.key = key;
        this.role = role;
        this.label = label;
        this.isChecked = isChecked;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }
}
