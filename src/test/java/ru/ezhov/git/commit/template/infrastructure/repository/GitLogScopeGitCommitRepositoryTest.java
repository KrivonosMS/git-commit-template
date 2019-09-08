package ru.ezhov.git.commit.template.infrastructure.repository;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class GitLogScopeGitCommitRepositoryTest {
    @Test
    public void scopesOfChange() {
        GitLogScopeGitCommitRepository gitLogScopeGitCommitRepository = new GitLogScopeGitCommitRepository(new File("."));
        gitLogScopeGitCommitRepository.scopesOfChange();
    }
}