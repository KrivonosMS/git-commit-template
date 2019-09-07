package ru.ezhov.git.commit.template.model.domain;

public class TypeOfChange {
    private String name;
    private String title;
    private String description;

    public TypeOfChange(String name, String title, String description) {
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
        return String.format("%s - %s", name, description);
    }
}
