package ru.ezhov.git.commit.template.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.JBSplitter;
import ru.ezhov.git.commit.template.infrastructure.repository.GitBranchTaskIdRepository;
import ru.ezhov.git.commit.template.infrastructure.repository.GitLogScopeGitCommitRepository;
import ru.ezhov.git.commit.template.infrastructure.repository.TypeAndScopeXmlGitCommitRepository;
import ru.ezhov.git.commit.template.model.domain.CommitMessage;
import ru.ezhov.git.commit.template.model.domain.ScopeOfChange;
import ru.ezhov.git.commit.template.model.domain.TaskId;
import ru.ezhov.git.commit.template.model.domain.TypeOfChange;
import ru.ezhov.git.commit.template.model.repository.ScopesOfChangeRepository;
import ru.ezhov.git.commit.template.model.repository.TaskIdRepository;
import ru.ezhov.git.commit.template.model.repository.TypesOfChangeRepository;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class CommitPanel extends JPanel {
    private JComboBox<TypeOfChange> changeType;
    private JComboBox<ScopeOfChange> changeScopeGit;
    private JComboBox<ScopeOfChange> changeScopeLocal;
    private JTextField taskIdPanel;
    private JTextField shortDescription;
    private JTextPane longDescription;
    private JTextField metaInformation;
    private JTextPane breakingChanges;
    private final TypesOfChangeRepository typesOfChangeRepository;
    private final ScopesOfChangeRepository scopesOfChangeRepositoryLocal;
    private final ScopesOfChangeRepository scopesOfChangeRepositoryGit;
    private final TaskIdRepository taskIdRepository;

    CommitPanel(Project project) {
        super(new BorderLayout());
        TypeAndScopeXmlGitCommitRepository typeAndScopeXmlGitCommitRepository = new TypeAndScopeXmlGitCommitRepository();
        this.typesOfChangeRepository = typeAndScopeXmlGitCommitRepository;
        this.scopesOfChangeRepositoryGit = new GitLogScopeGitCommitRepository(new File(project.getBasePath()));
        this.scopesOfChangeRepositoryLocal = typeAndScopeXmlGitCommitRepository;
        this.taskIdRepository = new GitBranchTaskIdRepository(new File(project.getBasePath()));

        add(new TopPanel(), BorderLayout.NORTH);
        add(new CenterPanel(), BorderLayout.CENTER);
        add(new BottomPanel(), BorderLayout.SOUTH);

        setPreferredSize(new Dimension(800, 600));
    }

    CommitMessage getCommitMessage() {
        ScopeOfChange scope = getScopeOfChange();
        return CommitMessage.from(
                new TaskId(taskIdPanel.getText().trim()),
                (TypeOfChange) changeType.getSelectedItem(),
                scope,
                shortDescription.getText().trim(),
                longDescription.getText().trim(),
                metaInformation.getText().trim(),
                breakingChanges.getText().trim()
        );
    }

    private ScopeOfChange getScopeOfChange() {
        ScopeOfChange scope = null;
        Object gitScopeOfChange = changeScopeGit.getSelectedItem();
        if (gitScopeOfChange != null && !"".equals(gitScopeOfChange)) {
            if (gitScopeOfChange instanceof String) {
                String scopeText = gitScopeOfChange.toString();
                scope = new ScopeOfChange(scopeText, scopeText, scopeText);
            } else {
                scope = (ScopeOfChange) changeScopeGit.getSelectedItem();
            }
        } else {
            Object localScopeOfChange = changeScopeLocal.getSelectedItem();
            if (localScopeOfChange != null && !"".equals(localScopeOfChange)) {
                if (localScopeOfChange instanceof String) {
                    String scopeText = localScopeOfChange.toString();
                    scope = new ScopeOfChange(scopeText, scopeText, scopeText);
                } else {
                    scope = (ScopeOfChange) changeScopeLocal.getSelectedItem();
                }
            }
        }


        return scope;
    }

    JComponent getFocusedComponent() {
        return changeType;
    }

    private class TopPanel extends JPanel {
        TopPanel() {
            super(new BorderLayout());
            JBSplitter northSplitPane = new JBSplitter();
            northSplitPane.setProportion(0.2F);
            northSplitPane.setFirstComponent(new TaskIdPanel());
            northSplitPane.setSecondComponent(new ChangeTypePanel());
            add(northSplitPane, BorderLayout.NORTH);

            JBSplitter centreSplitPane = new JBSplitter();
            centreSplitPane.setProportion(0.3F);
            centreSplitPane.setFirstComponent(new GitLogChangeScopeLocalPanel());
            centreSplitPane.setSecondComponent(new ChangeScopeLocalPanel());
            add(centreSplitPane, BorderLayout.CENTER);
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
            List<TypeOfChange> typeOfChanges = typesOfChangeRepository.typesOfChanges();
            TypeOfChange[] typeOfChangesArray = new TypeOfChange[typeOfChanges.size()];
            typeOfChanges.toArray(typeOfChangesArray);
            changeType = new ComboBox(typeOfChangesArray);

            setBorder(BorderFactory.createTitledBorder("Type of change"));
            add(changeType, BorderLayout.CENTER);
        }
    }

    private class ChangeScopeLocalPanel extends JPanel {
        ChangeScopeLocalPanel() {
            super(new BorderLayout());
            List<ScopeOfChange> scopeOfChanges = scopesOfChangeRepositoryLocal.scopesOfChange();
            List<ScopeOfChange> scopeOfChangesResult = new ArrayList<>();
            scopeOfChangesResult.add(new ScopeOfChange("", "", ""));
            scopeOfChangesResult.addAll(scopeOfChanges);
            ScopeOfChange[] scopeOfChangesArray = new ScopeOfChange[scopeOfChangesResult.size()];
            scopeOfChangesResult.toArray(scopeOfChangesArray);
            changeScopeLocal = new ComboBox<>(scopeOfChangesArray);

            setBorder(BorderFactory.createTitledBorder("Local scope of this change"));
            changeScopeLocal.setEditable(true);
            add(changeScopeLocal, BorderLayout.CENTER);
        }
    }

    private class GitLogChangeScopeLocalPanel extends JPanel {
        GitLogChangeScopeLocalPanel() {
            super(new BorderLayout());
            List<ScopeOfChange> scopeOfChanges = scopesOfChangeRepositoryGit.scopesOfChange();
            List<ScopeOfChange> scopeOfChangesResult = new ArrayList<>();
            scopeOfChangesResult.add(new ScopeOfChange("", "", ""));
            scopeOfChangesResult.addAll(scopeOfChanges);
            ScopeOfChange[] scopeOfChangesArray = new ScopeOfChange[scopeOfChangesResult.size()];
            scopeOfChangesResult.toArray(scopeOfChangesArray);
            changeScopeGit = new ComboBox<>(scopeOfChangesArray);

            setBorder(BorderFactory.createTitledBorder("Git scope of this change"));
            changeScopeGit.setEditable(true);
            add(changeScopeGit, BorderLayout.CENTER);
        }
    }

    private class TaskIdPanel extends JPanel {
        TaskIdPanel() {
            super(new BorderLayout());
            String taskId = taskIdRepository.taskId();
            taskIdPanel = new JTextField(taskId);
            setBorder(BorderFactory.createTitledBorder("Task id"));
            add(taskIdPanel, BorderLayout.CENTER);
        }
    }

    private class ShortDescriptionPanel extends JPanel {
        ShortDescriptionPanel() {
            super(new BorderLayout());
            shortDescription = new JTextField();
            setBorder(BorderFactory.createTitledBorder("Short description"));
            add(shortDescription, BorderLayout.CENTER);
        }
    }

    private class LongDescriptionPanel extends JPanel {
        LongDescriptionPanel() {
            super(new BorderLayout());
            longDescription = new JTextPane();
            setBorder(BorderFactory.createTitledBorder("Long description"));
            add(new JScrollPane(longDescription), BorderLayout.CENTER);
        }
    }

    private class BreakingChangesPanel extends JPanel {
        BreakingChangesPanel() {
            super(new BorderLayout());
            breakingChanges = new JTextPane();
            setBorder(BorderFactory.createTitledBorder("Breaking changes"));
            add(new JScrollPane(breakingChanges), BorderLayout.CENTER);
        }
    }

    private class MetaInformationPanel extends JPanel {
        MetaInformationPanel() {
            super(new BorderLayout());
            metaInformation = new JTextField();

            setBorder(BorderFactory.createTitledBorder("Meta information"));
            add(new JScrollPane(metaInformation), BorderLayout.CENTER);
        }
    }
}
