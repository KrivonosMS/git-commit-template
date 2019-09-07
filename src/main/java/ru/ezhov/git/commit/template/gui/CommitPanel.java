package ru.ezhov.git.commit.template.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import ru.ezhov.git.commit.template.ChangeType;
import ru.ezhov.git.commit.template.model.domain.CommitMessage;
import ru.ezhov.git.commit.template.model.domain.ScopeOfChange;
import ru.ezhov.git.commit.template.model.domain.TypeOfChange;
import ru.ezhov.git.commit.template.model.repository.GitCommitRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class CommitPanel extends JPanel {
    private JComboBox<TypeOfChange> changeType;
    private JComboBox<ScopeOfChange> changeScopeLocal;
    private JComboBox<ScopeOfChange> changeScopeGit;
    private JTextField shortDescription;
    private JTextPane longDescription;
    private JTextField metaInformation;
    private JTextPane breakingChanges;

    CommitPanel(GitCommitRepository gitCommitRepository, Project project) {
        super(new BorderLayout());

        List<TypeOfChange> typeOfChanges = gitCommitRepository.typesOfChanges();
        TypeOfChange[] typeOfChangesArray = new TypeOfChange[typeOfChanges.size()];
        typeOfChanges.toArray(typeOfChangesArray);
        changeType = new ComboBox(typeOfChangesArray);

        List<ScopeOfChange> scopeOfChanges = gitCommitRepository.localScopeOfChanges();
        ScopeOfChange[] scopeOfChangesArray = new ScopeOfChange[scopeOfChanges.size()];
        scopeOfChanges.toArray(scopeOfChangesArray);
        changeScopeLocal = new ComboBox(scopeOfChangesArray);

        scopeOfChanges = gitCommitRepository.gitLogScopeOfChanges();
        scopeOfChangesArray = new ScopeOfChange[scopeOfChanges.size()];
        scopeOfChanges.toArray(scopeOfChangesArray);
        changeScopeGit = new ComboBox<>(scopeOfChangesArray);

        shortDescription = new JTextField();
        longDescription = new JTextPane();
        breakingChanges = new JTextPane();
        metaInformation = new JTextField();

        add(new TopPanel(), BorderLayout.NORTH);
        add(new CenterPanel(), BorderLayout.CENTER);
        add(new BottomPanel(), BorderLayout.SOUTH);

//        for (ChangeType type : ChangeType.values()) {
//            changeType.addItem(type);
//        }
//        File workingDirectory = VfsUtil.virtualToIoFile(project.getBaseDir());
//        Command.Result result = new Command(workingDirectory, "git log --all --format=%s | grep -Eo '^[a-z]+(\\(.*\\)):.*$' | sed 's/^.*(\\(.*\\)):.*$/\\1/' | sort -n | uniq").execute();
//        if (result.isSuccess()) {
//            result.getOutput().forEach(changeScopeLocal::addItem);
//        }
    }

    CommitMessage getCommitMessage() {
//        return new CommitMessage(
//                ChangeType.BUILD,
//                "changeScopeLocal",
//                "shortDescription",
//                "longDescription",
//                "metaInformation",
//                "breakingChanges"
//        );
        return CommitMessage.from(
                (TypeOfChange) changeType.getSelectedItem(),
                (ScopeOfChange) changeScopeLocal.getSelectedItem(),
                shortDescription.getText().trim(),
                longDescription.getText().trim(),
                metaInformation.getText().trim(),
                breakingChanges.getText().trim()
        );
    }

    private class TopPanel extends JPanel {
        public TopPanel() {
            super(new BorderLayout());
            add(changeType, BorderLayout.NORTH);
            add(changeScopeLocal, BorderLayout.CENTER);
        }
    }

    private class CenterPanel extends JPanel {
        public CenterPanel() {
            super(new BorderLayout());
            add(shortDescription, BorderLayout.NORTH);
            add(new JScrollPane(longDescription), BorderLayout.CENTER);
        }
    }

    private class BottomPanel extends JPanel {
        public BottomPanel() {
            super(new BorderLayout());
            add(breakingChanges, BorderLayout.NORTH);
            add(metaInformation, BorderLayout.CENTER);
        }
    }
}
