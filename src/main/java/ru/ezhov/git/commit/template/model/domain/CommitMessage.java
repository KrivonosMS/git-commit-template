package ru.ezhov.git.commit.template.model.domain;

import org.apache.commons.lang.WordUtils;

import static org.apache.commons.lang.StringUtils.isNotBlank;

public class CommitMessage {
    private static final int MAX_LINE_LENGTH = 72; // https://stackoverflow.com/a/2120040/5138796
    private final String content;

    private CommitMessage(TaskId taskId, TypeOfChange changeType, ScopeOfChange changeScope, String shortDescription, String longDescription, String closedIssues, String breakingChanges) {
        this.content = buildContent(taskId, changeType, changeScope, shortDescription, longDescription, closedIssues, breakingChanges);
    }

    public static CommitMessage from(TaskId taskId, TypeOfChange changeType, ScopeOfChange changeScope, String shortDescription, String longDescription, String closedIssues, String breakingChanges) {
        return new CommitMessage(taskId, changeType, changeScope, shortDescription, longDescription, closedIssues, breakingChanges);
    }

    private String buildContent(TaskId taskId, TypeOfChange changeType, ScopeOfChange changeScope, String shortDescription, String longDescription, String closedIssues, String breakingChanges) {
        StringBuilder builder = new StringBuilder();
        if (taskId != null || isNotBlank(taskId.getId())) {
            builder
                    .append("[")
                    .append(taskId.getId())
                    .append("] ");
        }
        builder.append(changeType.getName());
        if (changeScope != null && isNotBlank(changeScope.getName())) {
            builder
                    .append('(')
                    .append(changeScope.getName())
                    .append(')');
        }
        builder
                .append(": ")
                .append(shortDescription)
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(WordUtils.wrap(longDescription, MAX_LINE_LENGTH));

        if (isNotBlank(breakingChanges)) {
            builder
                    .append(System.lineSeparator())
                    .append(System.lineSeparator())
                    .append(WordUtils.wrap("BREAKING CHANGE: " + breakingChanges, MAX_LINE_LENGTH));
        }

        if (isNotBlank(closedIssues)) {
            builder.append(System.lineSeparator());
            for (String closedIssue : closedIssues.split(",")) {
                builder
                        .append(System.lineSeparator())
                        .append(closedIssue);
            }
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return content;
    }
}