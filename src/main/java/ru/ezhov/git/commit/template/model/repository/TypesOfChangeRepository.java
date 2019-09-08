package ru.ezhov.git.commit.template.model.repository;

import ru.ezhov.git.commit.template.model.domain.TypeOfChange;

import java.util.List;

public interface TypesOfChangeRepository {
    List<TypeOfChange> typesOfChanges();
}
