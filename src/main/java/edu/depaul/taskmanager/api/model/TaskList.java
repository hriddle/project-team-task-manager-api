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

    private String listType = "list";

    public TaskList() {
    }

    private TaskList(Builder builder) {
        id = builder.id;
        name = builder.name;
        ownerId = builder.ownerId;
        tasks = builder.tasks;
        listType = builder.listType;
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
        builder.listType = copy.getListType();
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

    public String getListType() {
        return listType;
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", tasks=" + tasks +
                ", listType=" + listType +
                '}';

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskList taskList = (TaskList) o;
        return Objects.equals(id, taskList.id) &&
                Objects.equals(name, taskList.name) &&
                Objects.equals(ownerId, taskList.ownerId) &&
                Objects.equals(tasks, taskList.tasks) &&
                listType == taskList.listType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ownerId, tasks, listType);
    }

    public static final class Builder {
        private String id;
        private String name;
        private String ownerId;
        private List<Task> tasks;
        private String listType = "list";

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

        public Builder withTasks(List<Task> val) {
            tasks = val;
            return this;
        }

        public Builder withListType(String val) {
            listType = val;
            return this;
        }

        public TaskList build() {
            return new TaskList(this);
        }
    }
}
