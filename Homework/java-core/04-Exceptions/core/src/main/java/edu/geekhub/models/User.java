package edu.geekhub.models;

import java.util.UUID;

public class User {

    private final UUID id;
    private final String email;
    private final String userName;
    private final String fullName;
    private final Integer age;
    private final String notes;
    private final Long amountOfFollowers;

    private User(UUID id,
                String email,
                String userName,
                String fullName,
                Integer age,
                String notes,
                Long amountOfFollowers) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.fullName = fullName;
        this.age = age;
        this.notes = notes;
        this.amountOfFollowers = amountOfFollowers;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getAge() {
        return age;
    }

    public String getNotes() {
        return notes;
    }

    public Long getAmountOfFollowers() {
        return amountOfFollowers;
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", age=" + age +
                ", notes='" + notes + '\'' +
                ", amountOfFollowers=" + amountOfFollowers +
                '}';
    }

    public static UserBuilder1 builder() {
        return new UserBuilder1();
    }

    public static final class UserBuilder1 {
        private UUID id;
        private String email;
        private String userName;
        private String fullName;
        private Integer age;
        private String notes;
        private Long amountOfFollowers;

        private UserBuilder1() {
        }



        public UserBuilder1 id(UUID id) {
            this.id = id;
            return this;
        }

        public UserBuilder1 email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder1 userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder1 fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public UserBuilder1 age(Integer age) {
            this.age = age;
            return this;
        }

        public UserBuilder1 notes(String notes) {
            this.notes = notes;
            return this;
        }

        public UserBuilder1 amountOfFollowers(Long amountOfFollowers) {
            this.amountOfFollowers = amountOfFollowers;
            return this;
        }

        public User build() {
            return new User(id, email, userName, fullName, age, notes, amountOfFollowers);
        }
    }
}