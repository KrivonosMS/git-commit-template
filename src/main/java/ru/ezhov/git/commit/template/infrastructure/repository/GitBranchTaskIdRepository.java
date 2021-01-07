package ru.ezhov.git.commit.template.infrastructure.repository;

import ru.ezhov.git.commit.template.infrastructure.util.cli.Command;
import ru.ezhov.git.commit.template.model.repository.TaskIdRepository;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitBranchTaskIdRepository implements TaskIdRepository {
    private static final String GIT_LOG_COMMAND = "git rev-parse --abbrev-ref HEAD";

    private String taskId = "";

    private final File workingDirectory;

    public GitBranchTaskIdRepository(File workingDirectory) {
        this.workingDirectory = workingDirectory;
        initGitBranchTaskIdRepository();
    }

    @Override
    public String taskId() {
        return taskId;
    }

    private void initGitBranchTaskIdRepository() {
        Command.Result result = new Command(workingDirectory, GIT_LOG_COMMAND).execute();
        if (result.isSuccess()) {
            String output = result.getOutput();
            Pattern pattern = Pattern.compile("\\w+-\\d+");
            Matcher matcher = pattern.matcher(output);
            if (matcher.find()) {
                taskId= matcher.group();
            }
        }
    }
}
