package com.skillspace.user.entity;

public enum NotificationPreference {
    ALL_NEW_MESSAGES("All New Messages"),
    APPLICATION_MESSAGES("Application Messages");

    private final String displayName;

    NotificationPreference(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
