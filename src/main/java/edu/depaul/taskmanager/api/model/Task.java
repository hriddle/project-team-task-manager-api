package edu.depaul.taskmanager.api.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private LocalDateTime dueDate;

    public Task() {
    }

    private Task(Builder builder) {
        name = builder.name;
        dueDate = builder.dueDate;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", dueDate=" + dueDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                Objects.equals(dueDate, task.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dueDate);
    }

    public static final class Builder {
        private String name;
        private LocalDateTime dueDate;

        private Builder() {
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withDueDate(LocalDateTime val) {
            dueDate = val;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
