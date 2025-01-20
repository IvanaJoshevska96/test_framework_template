package XMLDataManipulation;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class XmlGuidUpdater {
    public static String generateValidGuid() {
        return UUID.randomUUID().toString().toLowerCase();
    }

    public static String generateRandomId() {
        Random random = new Random();
        return String.format("%03d", random.nextInt(1000));
    }

    public static void updateFieldInXml(String inputFilePath, String outputFilePath, String fieldType) throws Exception {
        Set<String> usedGuids = new HashSet<>();
        File inputFile = new File(inputFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        NodeList elements = doc.getElementsByTagName("*");
        for (int i = 0; i < elements.getLength(); i++) {
            Node element = elements.item(i);

            if (fieldType.equalsIgnoreCase("guid") && element.getNodeName().equalsIgnoreCase("guid")) {
                String newGuid = generateUniqueGuid(usedGuids);
                element.setTextContent(newGuid);
            } else if (fieldType.equalsIgnoreCase("id") && element.getNodeName().equalsIgnoreCase("id")) {
                String newId = generateRandomId();
                element.setTextContent(newId);
            }

            if (element.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) element;
                NamedNodeMap attributes = eElement.getAttributes();
                for (int j = 0; j < attributes.getLength(); j++) {
                    Attr attr = (Attr) attributes.item(j);
                    if (fieldType.equalsIgnoreCase("guid") && attr.getName().equalsIgnoreCase("guid")) {
                        String newGuid = generateUniqueGuid(usedGuids);
                        attr.setValue(newGuid);
                    } else if (fieldType.equalsIgnoreCase("id") && attr.getName().equalsIgnoreCase("id")) {
                        String newId = generateRandomId();
                        attr.setValue(newId);
                    }
                }
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(outputFilePath));
        transformer.transform(source, result);
    }

    private static String generateUniqueGuid(Set<String> usedGuids) {
        String newGuid;
        do {
            newGuid = generateValidGuid();
        } while (usedGuids.contains(newGuid));
        usedGuids.add(newGuid);
        return newGuid;
    }

    public static void main(String[] args) throws Exception {
        String inputFilePath = "input.xml";
        String outputFilePath = "output.xml";

        String fieldType = "guid";

        updateFieldInXml(inputFilePath, outputFilePath, fieldType);
        System.out.println("Fields updated successfully.");
    }
}
