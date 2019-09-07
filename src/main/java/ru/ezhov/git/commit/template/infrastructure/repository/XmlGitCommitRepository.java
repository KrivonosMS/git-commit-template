package ru.ezhov.git.commit.template.infrastructure.repository;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ru.ezhov.git.commit.template.model.domain.ScopeOfChange;
import ru.ezhov.git.commit.template.model.domain.TypeOfChange;
import ru.ezhov.git.commit.template.model.repository.GitCommitRepository;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class XmlGitCommitRepository implements GitCommitRepository {
    private static final String STORE_NAME = "git-store.xml";
    private static final String INNER_STORE_PATH = "/store";
    private static final String EXTERNAL_STORE_DIRECTORY_PATH = System.getProperty("user.home") + File.separator + ".git-commit-template";

    private List<TypeOfChange> typeOfChanges = new ArrayList<>();
    private List<ScopeOfChange> localScopeOfChanges = new ArrayList<>();
    private List<ScopeOfChange> gitLogScopeOfChanges = new ArrayList<>();

    public XmlGitCommitRepository() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException, XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        try (InputStream store = getStore()) {
            NodeList typeOfChangesNodeList = (NodeList) xPath.evaluate("/git-commit/types-of-change/type-of-change", new InputSource(store), XPathConstants.NODESET);
            if (typeOfChangesNodeList.getLength() > 0) {
                fillTypeOfChanges(typeOfChangesNodeList);
            }
            NodeList scopesOfChangeNodeList = (NodeList) xPath.evaluate("/git-commit/scopes-of-change/scope-of-change", new InputSource(store), XPathConstants.NODESET);
            if (scopesOfChangeNodeList.getLength() > 0) {
                fillScopesOfChanges(scopesOfChangeNodeList);
            }
        }
    }

    private InputStream getStore() throws FileNotFoundException {
        File externalStore = new File(EXTERNAL_STORE_DIRECTORY_PATH, STORE_NAME);
        if (externalStore.exists()) {
            return new FileInputStream(externalStore);
        } else {
            try {
                new File(EXTERNAL_STORE_DIRECTORY_PATH).mkdirs();
                copyInnerStoreToExternalStore(externalStore);
                return new FileInputStream(externalStore);
            } catch (IOException e) {
                return innerStoreStream();
            }
        }
    }

    private InputStream innerStoreStream() {
        return getClass().getResourceAsStream(INNER_STORE_PATH + "/" + STORE_NAME);
    }

    private void copyInnerStoreToExternalStore(File externalStore) throws IOException {
        try (Scanner scanner = new Scanner(innerStoreStream(), "UTF-8")) {
            try (OutputStreamWriter fileOutputStream = new OutputStreamWriter(new FileOutputStream(externalStore), StandardCharsets.UTF_8)) {
                while (scanner.hasNextLine()) {
                    fileOutputStream.append(scanner.nextLine()).append(System.lineSeparator());
                }
            }
        }
    }

    private void fillTypeOfChanges(NodeList typeOfChangesNodeSet) {
        int length = typeOfChangesNodeSet.getLength();
        for (int i = 0; i < length; i++) {
            Node node = typeOfChangesNodeSet.item(i);
            NodeList childNodes = node.getChildNodes();
            String name = "";
            String title = "";
            String description = "";
            for (int r = 0; r < childNodes.getLength(); r++) {
                Node item = childNodes.item(r);
                String nodeName = childNodes.item(r).getNodeName();
                String textContent = item.getTextContent();
                if ("name".equals(nodeName)) {
                    name = textContent;
                } else if ("title".equals(nodeName)) {
                    title = textContent;
                } else if ("description".equals(nodeName)) {
                    description = textContent;
                }
            }
            typeOfChanges.add(new TypeOfChange(name, title, description));
        }
    }

    private void fillScopesOfChanges(NodeList scopesOPfChangeNodeSet) {
        int length = scopesOPfChangeNodeSet.getLength();
        for (int i = 0; i < length; i++) {
            Node node = scopesOPfChangeNodeSet.item(i);
            NodeList childNodes = node.getChildNodes();
            String name = "";
            String title = "";
            String description = "";
            for (int r = 0; r < childNodes.getLength(); r++) {
                Node item = childNodes.item(r);
                String nodeName = childNodes.item(r).getNodeName();
                String textContent = item.getTextContent();
                if ("name".equals(nodeName)) {
                    name = textContent;
                } else if ("title".equals(nodeName)) {
                    title = textContent;
                } else if ("description".equals(nodeName)) {
                    description = textContent;
                }
            }
            localScopeOfChanges.add(new ScopeOfChange(name, title, description));
        }
    }

    @Override
    public List<TypeOfChange> typesOfChanges() {
        return typeOfChanges;
    }

    @Override
    public List<ScopeOfChange> localScopeOfChanges() {
        return localScopeOfChanges;
    }

    @Override
    public List<ScopeOfChange> gitLogScopeOfChanges() {
        return gitLogScopeOfChanges;
    }
}
