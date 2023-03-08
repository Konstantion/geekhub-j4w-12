package com.konstantion.user;

import java.util.UUID;

public record User(UUID uuid, String username, String email, String password) {

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static final class UserBuilder {
        private UUID uuid;
        private String username;
        private String email;
        private String password;

        private UserBuilder() {
        }

        public UserBuilder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(uuid, username, email, password);
        }
    }
}
