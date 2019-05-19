package edu.depaul.taskmanager.api.model;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;

public class TaskList {
    @Id
    private String id;
    private String name;
    private String ownerId;
    private List<Task> tasks;

    public TaskList() {
    }

    private TaskList(Builder builder) {
        id = builder.id;
        name = builder.name;
        ownerId = builder.ownerId;
        tasks = builder.tasks;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(TaskList copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.name = copy.getName();
        builder.ownerId = copy.getOwnerId();
        builder.tasks = copy.getTasks();
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

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", tasks=" + tasks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskList that = (TaskList) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(tasks, that.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ownerId, tasks);
    }

    public static final class Builder {
        private String id;
        private String name;
        private String ownerId;
        private List<Task> tasks;

        private Builder() {
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withOwnerId(String ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public Builder withTasks(List<Task> tasks) {
            this.tasks = tasks;
            return this;
        }

        public TaskList build() {
            return new TaskList(this);
        }
    }
}
