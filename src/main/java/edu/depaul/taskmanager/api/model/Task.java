package edu.depaul.taskmanager.api.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private LocalDateTime dueDate;
    private String assignedUser;
    private CompletionDetails completionDetails;

    public Task() {
    }

    private Task(Builder builder) {
        name = builder.name;
        dueDate = builder.dueDate;
        assignedUser = builder.assignedUser;
        completionDetails = builder.completionDetails;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Task copy) {
        Builder builder = new Builder();
        builder.name = copy.getName();
        builder.dueDate = copy.getDueDate();
        builder.assignedUser = copy.getAssignedUser();
        builder.completionDetails = copy.getCompletionDetails();
        return builder;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public CompletionDetails getCompletionDetails() {
        return completionDetails;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", dueDate=" + dueDate +
                ", assignedUser='" + assignedUser + '\'' +
                ", completionDetails=" + completionDetails +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                Objects.equals(dueDate, task.dueDate) &&
                Objects.equals(assignedUser, task.assignedUser) &&
                Objects.equals(completionDetails, task.completionDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dueDate, assignedUser, completionDetails);
    }

    public static final class Builder {
        private String name;
        private LocalDateTime dueDate;
        private String assignedUser;
        private CompletionDetails completionDetails;

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

        public Builder withAssignedUser(String val) {
            this.assignedUser = val;
            return this;
        }

        public Builder withCompletionDetails(CompletionDetails completionDetails) {
            this.completionDetails = completionDetails;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
