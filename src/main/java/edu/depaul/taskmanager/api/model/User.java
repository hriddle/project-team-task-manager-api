package edu.depaul.taskmanager.api.model;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class User {
    @Id
    private String id;

    private String email;
    private String password;

    private String firstName;
    private String lastName;

    public User() {
    }

    private User(Builder builder) {
        id = builder.id;
        email = builder.email;
        password = builder.password;
        firstName = builder.firstName;
        lastName = builder.lastName;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(User copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.email = copy.getEmail();
        builder.password = copy.getPassword();
        builder.firstName = copy.getFirstName();
        builder.lastName = copy.getLastName();
        return builder;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, firstName, lastName);
    }

    public static final class Builder {
        private String id;
        private String email;
        private String password;
        private String firstName;
        private String lastName;

        private Builder() {
        }

        public Builder withId(String val) {
            id = val;
            return this;
        }

        public Builder withEmail(String val) {
            email = val;
            return this;
        }

        public Builder withPassword(String val) {
            password = val;
            return this;
        }

        public Builder withFirstName(String val) {
            firstName = val;
            return this;
        }

        public Builder withLastName(String val) {
            lastName = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
