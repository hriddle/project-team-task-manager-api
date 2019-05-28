package edu.depaul.taskmanager.api.model;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;


public class Team {
    @Id
    private String id;
    private String name;
    private List<TeamMember> members;

    public Team() {
    }

    private Team(Builder builder) {
        id = builder.id;
        name = builder.name;
        members = builder.members;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Team copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.name = copy.getName();
        builder.members = copy.getMembers();
        return builder;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<TeamMember> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", members=" + members +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id) &&
                Objects.equals(name, team.name) &&
                Objects.equals(members, team.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, members);
    }

    public static final class Builder {
        private String id;
        private String name;
        private List<TeamMember> members;

        private Builder() {
        }

        public Builder withId(String val) {
            id = val;
            return this;
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withMembers(List<TeamMember> val) {
            members = val;
            return this;
        }

        public Team build() {
            return new Team(this);
        }
    }
}
