package ru.ezhov.git.commit.template.model.domain;

public class ScopeOfChange {
    private final String name;
    private final String title;
    private final String description;

    public ScopeOfChange(String name, String title, String description) {
        this.name = name;
        this.title = title;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name;
    }
}
