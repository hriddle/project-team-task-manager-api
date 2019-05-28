package edu.depaul.taskmanager.api.model;

import java.util.Objects;

public class TeamMember {

    private String id;

    public TeamMember() {
    }

    protected TeamMember(String id) {
        this.id = id;
    }

    private TeamMember(Builder builder) {
        id = builder.id;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(TeamMember copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        return builder;
    }


    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TeamMember{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamMember that = (TeamMember) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static final class Builder {
        private String id;

        private Builder() {
        }

        public Builder withId(String val) {
            id = val;
            return this;
        }

        public TeamMember build() {
            return new TeamMember(this);
        }
    }
}
