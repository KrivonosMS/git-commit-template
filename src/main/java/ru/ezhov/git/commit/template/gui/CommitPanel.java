package ru.ezhov.git.commit.template.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.JBSplitter;
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
    private GitCommitRepository gitCommitRepository;

    CommitPanel(GitCommitRepository gitCommitRepository, Project project) {
        super(new BorderLayout());
        this.gitCommitRepository = gitCommitRepository;

        //        scopeOfChanges = gitCommitRepository.gitLogScopeOfChanges();
//        scopeOfChangesArray = new ScopeOfChange[scopeOfChanges.size()];
//        scopeOfChanges.toArray(scopeOfChangesArray);
//        changeScopeGit = new ComboBox<>(scopeOfChangesArray);

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

        setPreferredSize(new Dimension(600, 600));
    }

    CommitMessage getCommitMessage() {
        Object scopeOfChange = changeScopeLocal.getSelectedItem();
        ScopeOfChange scope = null;
        if (scopeOfChange != null) {
            if (scopeOfChange instanceof String) {
                String scopeText = scopeOfChange.toString();
                scope = new ScopeOfChange(scopeText, scopeText, scopeText);
            } else {
                scope = (ScopeOfChange) changeScopeLocal.getSelectedItem();
            }
        }

        return CommitMessage.from(
                (TypeOfChange) changeType.getSelectedItem(),
                scope,
                shortDescription.getText().trim(),
                longDescription.getText().trim(),
                metaInformation.getText().trim(),
                breakingChanges.getText().trim()
        );
    }

    JComponent getFocusedComponent() {
        return changeType;
    }

    private class TopPanel extends JPanel {
        TopPanel() {
            super(new BorderLayout());
            add(new ChangeTypePanel(), BorderLayout.NORTH);
            add(new ChangeScopeLocalPanel(), BorderLayout.CENTER);
        }
    }

    private class CenterPanel extends JPanel {
        CenterPanel() {
            super(new BorderLayout());
            add(new ShortDescriptionPanel(), BorderLayout.NORTH);

            JBSplitter splitPane = new JBSplitter(true);
            splitPane.setProportion(0.7F);
            splitPane.setFirstComponent(new LongDescriptionPanel());
            splitPane.setSecondComponent(new BreakingChangesPanel());
            add(splitPane, BorderLayout.CENTER);
        }
    }

    private class BottomPanel extends JPanel {
        BottomPanel() {
            super(new BorderLayout());
            add(new MetaInformationPanel(), BorderLayout.CENTER);
        }
    }


    private class ChangeTypePanel extends JPanel {
        ChangeTypePanel() {
            super(new BorderLayout());
            List<TypeOfChange> typeOfChanges = gitCommitRepository.typesOfChanges();
            TypeOfChange[] typeOfChangesArray = new TypeOfChange[typeOfChanges.size()];
            typeOfChanges.toArray(typeOfChangesArray);
            changeType = new ComboBox(typeOfChangesArray);

            setBorder(BorderFactory.createTitledBorder("Type of change"));
//            JLabel label = new JLabel("Type of change");
//            add(label, BorderLayout.NORTH);
            add(changeType, BorderLayout.CENTER);
        }
    }

    private class ChangeScopeLocalPanel extends JPanel {
        ChangeScopeLocalPanel() {
            super(new BorderLayout());
            List<ScopeOfChange> scopeOfChanges = gitCommitRepository.localScopeOfChanges();
            ScopeOfChange[] scopeOfChangesArray = new ScopeOfChange[scopeOfChanges.size()];
            scopeOfChanges.toArray(scopeOfChangesArray);
            changeScopeLocal = new ComboBox(scopeOfChangesArray);

            setBorder(BorderFactory.createTitledBorder("Scope of this change"));
//            JLabel label = new JLabel("Scope of this change");
//            add(label, BorderLayout.NORTH);
            changeScopeLocal.setEditable(true);
            add(changeScopeLocal, BorderLayout.CENTER);
        }
    }

    private class ShortDescriptionPanel extends JPanel {
        ShortDescriptionPanel() {
            super(new BorderLayout());
            shortDescription = new JTextField();
            setBorder(BorderFactory.createTitledBorder("Short description"));
//            JLabel label = new JLabel("Scope of this change");
//            add(label, BorderLayout.NORTH);
            add(shortDescription, BorderLayout.CENTER);
        }
    }

    private class LongDescriptionPanel extends JPanel {
        LongDescriptionPanel() {
            super(new BorderLayout());
            longDescription = new JTextPane();
            setBorder(BorderFactory.createTitledBorder("Long description"));
//            JLabel label = new JLabel("Scope of this change");
//            add(label, BorderLayout.NORTH);
            add(new JScrollPane(longDescription), BorderLayout.CENTER);
        }
    }

    private class BreakingChangesPanel extends JPanel {
        BreakingChangesPanel() {
            super(new BorderLayout());
            breakingChanges = new JTextPane();
            setBorder(BorderFactory.createTitledBorder("Breaking changes"));
//            JLabel label = new JLabel("Scope of this change");
//            add(label, BorderLayout.NORTH);
            add(new JScrollPane(breakingChanges), BorderLayout.CENTER);
        }
    }

    private class MetaInformationPanel extends JPanel {
        MetaInformationPanel() {
            super(new BorderLayout());
            metaInformation = new JTextField();

            setBorder(BorderFactory.createTitledBorder("Meta information"));
//            JLabel label = new JLabel("Scope of this change");
//            add(label, BorderLayout.NORTH);
            add(new JScrollPane(metaInformation), BorderLayout.CENTER);
        }
    }
}
