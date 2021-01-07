package ru.ezhov.git.commit.template.model.domain;

public class TaskId {
    private final String id;

    public TaskId(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "id";
    }
}
