package edu.depaul.taskmanager.api.model;

import java.util.Objects;

public class Session {
    private String firstName;
    private String lastName;
    private String userId;

    private Session(Builder builder) {
        firstName = builder.firstName;
        lastName = builder.lastName;
        userId = builder.userId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Session copy) {
        Builder builder = new Builder();
        builder.firstName = copy.getFirstName();
        builder.lastName = copy.getLastName();
        builder.userId = copy.getUserId();
        return builder;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Session{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(firstName, session.firstName) &&
                Objects.equals(lastName, session.lastName) &&
                Objects.equals(userId, session.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, userId);
    }

    public static final class Builder {
        private String firstName;
        private String lastName;
        private String userId;

        private Builder() {
        }

        public Builder withFirstName(String val) {
            firstName = val;
            return this;
        }

        public Builder withLastName(String val) {
            lastName = val;
            return this;
        }

        public Builder withUserId(String val) {
            userId = val;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }
}
