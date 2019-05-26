package edu.depaul.taskmanager.api.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private LocalDateTime dueDate;
    private CompletionDetails completionDetails;

    public Task() {
    }

    private Task(Builder builder) {
        name = builder.name;
        dueDate = builder.dueDate;
        completionDetails = builder.completionDetails;
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

    public CompletionDetails getCompletionDetails() {
        return completionDetails;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", dueDate=" + dueDate +
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
                Objects.equals(completionDetails, task.completionDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dueDate, completionDetails);
    }

    public static final class Builder {
        private String name;
        private LocalDateTime dueDate;
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

        public Builder withCompletionDetails(CompletionDetails completionDetails) {
            this.completionDetails = completionDetails;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
