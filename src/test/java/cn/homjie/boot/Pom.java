package cn.homjie.boot;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 展示项目之间的模块依赖
 *
 * @author jiehong.jh
 * @date 2018/11/30
 */
public class Pom {

    public static void main(String[] args) throws IOException {
        File project = project();

        List<File> poms = new ArrayList<>();
        collectPom(project, poms);

        List<String> modules = new ArrayList<>();
        List<PomData> dataList = poms.stream()
            .map(Pom::getDependencies)
            .peek(pomData -> modules.add(pomData.identity))
            .collect(Collectors.toList());

        for (PomData pomData : dataList) {
            System.out.println("-->" + pomData.identity);
            pomData.dependencies.stream()
                .filter(dependency -> modules.contains(dependency))
                .forEach(System.out::println);
            System.out.println();
        }

    }

    private static File project() throws IOException {
        File directory = new File(".");
        directory = new File(directory.getCanonicalPath());
        return directory;
    }

    private static void collectPom(File directory, List<File> poms) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File f : files) {
                if (f.isFile() && f.getName().equals("pom.xml")) {
                    poms.add(f);
                    continue;
                }
                if (f.isDirectory()) {
                    collectPom(f, poms);
                }
            }
        }
    }

    private static PomData getDependencies(File pom) {
        PomData pomData = new PomData();

        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(pom);
            Element rootNode = document.getRootElement();
            List<Element> list = rootNode.getChildren();
            for (int i = 0; i < list.size(); i++) {
                Element node = list.get(i);
                String name = node.getName();
                if (name.equalsIgnoreCase("parent")) {
                    List<Element> children = node.getChildren();
                    for (int k = 0; k < children.size(); k++) {
                        Element element = children.get(k);
                        String elementName = element.getName();
                        if (elementName.equalsIgnoreCase("artifactId") && pomData.artifactId == null) {
                            pomData.artifactId = element.getTextTrim();
                        }
                        if (elementName.equalsIgnoreCase("groupId") && pomData.groupId == null) {
                            pomData.groupId = element.getTextTrim();
                        }
                    }
                }
                if (name.equalsIgnoreCase("artifactId")) {
                    pomData.artifactId = node.getTextTrim();
                }
                if (name.equalsIgnoreCase("groupId")) {
                    pomData.groupId = node.getTextTrim();
                }

                if (name.equalsIgnoreCase("dependencies")) {
                    List<Element> children = node.getChildren();
                    for (int j = 0; j < children.size(); j++) {
                        Element element = children.get(j);
                        String elementName = element.getName();
                        if (elementName.equalsIgnoreCase("dependency")) {
                            HashMap<String, String> dependency = new HashMap<>();
                            List<Element> elementChildren = element.getChildren();
                            for (int k = 0; k < elementChildren.size(); k++) {
                                Element ele = elementChildren.get(k);
                                String eleName = ele.getName();
                                if (eleName.equalsIgnoreCase("groupId")
                                    || eleName.equalsIgnoreCase("artifactId")
                                    || eleName.equalsIgnoreCase("version")) {
                                    dependency.put(eleName, ele.getTextTrim());
                                }
                            }
                            pomData.dependencies.add(dependency.get("groupId") + ":" + dependency.get("artifactId"));
                        }
                    }
                }
            }
        } catch (Exception io) {
            System.out.println(io.getMessage());
        }
        return pomData.ok();
    }

    private static class PomData {
        String groupId;
        String artifactId;
        String identity;
        ArrayList<String> dependencies = new ArrayList<>();

        PomData ok() {
            identity = groupId + ":" + artifactId;
            return this;
        }
    }
}
