package edu.depaul.taskmanager.api.model;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class TaskList {
    @Id
    private String id;
    private String name;
    private String ownerId;

    public TaskList() {
    }

    private TaskList(Builder builder) {
        id = builder.id;
        name = builder.name;
        ownerId = builder.ownerId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(TaskList copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.name = copy.getName();
        builder.ownerId = copy.getOwnerId();
        return builder;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskList that = (TaskList) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ownerId);
    }

    public static final class Builder {
        private String id;
        private String name;
        private String ownerId;

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

        public Builder withOwnerId(String val) {
            ownerId = val;
            return this;
        }

        public TaskList build() {
            return new TaskList(this);
        }
    }
}
