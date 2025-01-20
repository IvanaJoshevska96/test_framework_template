package XMLDataManipulation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlDuplicateChecker {
    public static boolean checkForDuplicateField(String filename, String fieldName) throws Exception {
        Map<String, List<Element>> chunks = new HashMap<>();
        File inputFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("*");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.getTagName().equalsIgnoreCase(fieldName)) {
                    String textContent = element.getTextContent();
                    chunks.computeIfAbsent(textContent, k -> new ArrayList<>()).add(element);
                }
            }
        }

        for (List<Element> elements : chunks.values()) {
            if (elements.size() > 1) {
                return false;
            }
        }

        return true;
    }
}
