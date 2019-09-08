package ru.ezhov.git.commit.template.infrastructure.repository;

import org.junit.Test;
import ru.ezhov.git.commit.template.model.domain.ScopeOfChange;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class GitLogScopeGitCommitRepositoryTest {
    @Test
    public void scopesOfChange() {
        GitLogScopeGitCommitRepository gitLogScopeGitCommitRepository = new GitLogScopeGitCommitRepository(new File("."));

        List<ScopeOfChange> scopeOfChanges = gitLogScopeGitCommitRepository.scopesOfChange();

        scopeOfChanges.forEach(System.out::println);
    }
}