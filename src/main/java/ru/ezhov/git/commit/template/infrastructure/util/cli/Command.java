package ru.ezhov.git.commit.template.infrastructure.util.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Command {
    private final File workingDirectory;
    private final String command;

    public Command(File workingDirectory, String command) {
        this.workingDirectory = workingDirectory;
        this.command = command;
    }

    public static class Result {
        static Result ERROR = new Result(-1);

        private final int exitValue;
        private final String output;

        Result(int exitValue) {
            this.exitValue = exitValue;
            this.output = null;
        }

        Result(int exitValue, String output) {
            this.exitValue = exitValue;
            this.output = output;
        }

        public boolean isSuccess() {
            return exitValue == 0;
        }

        public String getOutput() {
            return output;
        }
    }

    public Result execute() {
        try {
            Process process;
            if (isWindows()) {
                process = windowsProcess();
            } else {
                process = unixProcces();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder stringBuilder = new StringBuilder();
            reader.lines().forEach(l -> stringBuilder.append(l).append(System.lineSeparator()));

            process.waitFor(2, TimeUnit.SECONDS);
            process.destroy();
            process.waitFor();
            return new Result(process.exitValue(), stringBuilder.toString());
        } catch (Exception e) {
            return Result.ERROR;
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private Process windowsProcess() throws IOException {
        return new ProcessBuilder("cmd", "/c", this.command)
                .directory(workingDirectory)
                .start();
    }

    private Process unixProcces() throws IOException {
        return new ProcessBuilder("/bin/sh", "-c", this.command)
                .directory(workingDirectory)
                .start();
    }

}