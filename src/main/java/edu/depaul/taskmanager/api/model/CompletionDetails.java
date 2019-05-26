package edu.depaul.taskmanager.api.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class CompletionDetails {
    private String completedBy;
    private LocalDateTime completedDate;

    public CompletionDetails() {
    }

    private CompletionDetails(Builder builder) {
        completedBy = builder.completedBy;
        completedDate = builder.completedDate;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    @Override
    public String toString() {
        return "CompletionDetails{" +
                "completedBy='" + completedBy + '\'' +
                ", completedDate=" + completedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletionDetails that = (CompletionDetails) o;
        return Objects.equals(completedBy, that.completedBy) &&
                Objects.equals(completedDate, that.completedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(completedBy, completedDate);
    }

    public static final class Builder {
        private String completedBy;
        private LocalDateTime completedDate;

        private Builder() {
        }

        public Builder withCompletedBy(String completedBy) {
            this.completedBy = completedBy;
            return this;
        }

        public Builder withCompletedDate(LocalDateTime completedDate) {
            this.completedDate = completedDate;
            return this;
        }

        public CompletionDetails build() {
            return new CompletionDetails(this);
        }
    }
}
