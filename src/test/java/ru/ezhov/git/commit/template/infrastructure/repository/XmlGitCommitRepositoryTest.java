package ru.ezhov.git.commit.template.infrastructure.repository;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlGitCommitRepositoryTest {

    @Test
    public void typesOfChanges() {
        XmlGitCommitRepository repository = new XmlGitCommitRepository();
        assertNotEquals(0, repository.typesOfChanges());
    }

    @Test
    public void localScopeOfChanges() {
    }

    @Test
    public void gitLogScopeOfChanges() {
    }
}