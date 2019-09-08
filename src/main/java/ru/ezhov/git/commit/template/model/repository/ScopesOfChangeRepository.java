package ru.ezhov.git.commit.template.model.repository;

import ru.ezhov.git.commit.template.model.domain.ScopeOfChange;

import java.util.List;

public interface ScopesOfChangeRepository {
    List<ScopeOfChange> scopesOfChange();
}
