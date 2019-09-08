package ru.ezhov.git.commit.template.infrastructure.repository;

import ru.ezhov.git.commit.template.infrastructure.util.cli.Command;
import ru.ezhov.git.commit.template.model.domain.ScopeOfChange;
import ru.ezhov.git.commit.template.model.repository.ScopesOfChangeRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GitLogScopeGitCommitRepository implements ScopesOfChangeRepository {
    private static final String GIT_LOG_COMMAND = "git log --all --format=%s";

    private List<ScopeOfChange> gitLogScopeOfChanges = new ArrayList<>();

    private File workingDirectory;

    public GitLogScopeGitCommitRepository(File workingDirectory) {
        this.workingDirectory = workingDirectory;
        initGitLogScopeOfChanges();

    }

    @Override
    public List<ScopeOfChange> scopesOfChange() {
        return gitLogScopeOfChanges;
    }

    private void initGitLogScopeOfChanges() {
        Command.Result result = new Command(workingDirectory, GIT_LOG_COMMAND).execute();
        if (result.isSuccess()) {
            Set<String> scopes = new HashSet<>();
            String output = result.getOutput();
            Pattern pattern = Pattern.compile("^[a-z]+(\\(.*\\)):");
            String[] lines = output.split(System.lineSeparator());
            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String find = matcher.group();
                    String clearText = clearText(find);
                    scopes.add(clearText);
                }
            }

            scopes.stream().sorted(String::compareTo).forEach(s -> {
                gitLogScopeOfChanges.add(new ScopeOfChange(s, s, s) {
                    @Override
                    public String toString() {
                        return getName();
                    }
                });

            });
        }
    }

    private String clearText(String text) {
        int first = text.indexOf('(');
        int last = text.lastIndexOf(')');
        return text.substring(first + 1, last);
    }
}