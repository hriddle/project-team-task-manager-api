package edu.depaul.taskmanager.api.model;

import java.util.Objects;

public class TeamMembers {

    private String id;

    public TeamMembers() {
    }

    private TeamMembers(Builder builder) {
        id = builder.id;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(TeamMembers copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        return builder;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TeamMembers{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamMembers that = (TeamMembers) o;
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

        public TeamMembers build() {
            return new TeamMembers(this);
        }
    }
}
