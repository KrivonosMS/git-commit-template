package ru.ezhov.git.commit.template.model.repository;

import ru.ezhov.git.commit.template.model.domain.ScopeOfChange;
import ru.ezhov.git.commit.template.model.domain.TypeOfChange;

import java.util.List;

public interface GitCommitRepository {
    List<TypeOfChange> typesOfChanges();

    List<ScopeOfChange> localScopeOfChanges();

    List<ScopeOfChange> gitLogScopeOfChanges();
}
