package ru.ezhov.git.commit.template.infrastructure.repository;

import org.junit.Test;

import static org.junit.Assert.*;

public class TypeAndScopeXmlGitCommitRepositoryTest {

    @Test
    public void typesOfChanges() {
        TypeAndScopeXmlGitCommitRepository repository = new TypeAndScopeXmlGitCommitRepository();
        assertNotEquals(0, repository.typesOfChanges());
    }

    @Test
    public void scopesOfChanges() {
    }

}