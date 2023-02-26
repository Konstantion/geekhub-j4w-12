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
}