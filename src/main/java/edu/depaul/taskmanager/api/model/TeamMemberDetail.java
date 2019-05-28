package edu.depaul.taskmanager.api.model;

import java.util.Objects;

public class TeamMemberDetail {

    private String id;
    private String firstName;
    private String lastName;

    public TeamMemberDetail(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private TeamMemberDetail(Builder builder) {
        id = builder.id;
        firstName = builder.firstName;
        lastName = builder.lastName;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(TeamMemberDetail copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.firstName = copy.getFirstName();
        builder.lastName = copy.getLastName();
        return builder;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TeamMemberDetail{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamMemberDetail that = (TeamMemberDetail) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }

    public static final class Builder {
        private String id;
        private String firstName;
        private String lastName;

        private Builder() {
        }

        public Builder withId(String val) {
            id = val;
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

        public TeamMemberDetail build() {
            return new TeamMemberDetail(this);
        }
    }
}
