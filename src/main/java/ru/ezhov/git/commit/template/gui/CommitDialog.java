package ru.ezhov.git.commit.template.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import ru.ezhov.git.commit.template.model.domain.CommitMessage;

import javax.swing.JComponent;

public class CommitDialog extends DialogWrapper {

    private final CommitPanel panel;

    CommitDialog(@Nullable Project project) {
        super(project);
        panel = new CommitPanel(project);
        setTitle("Commit");
        setOKButtonText("OK");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return panel.getFocusedComponent();
    }

    CommitMessage getCommitMessage() {
        return panel.getCommitMessage();
    }

}
