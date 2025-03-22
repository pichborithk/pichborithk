import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class XmlToJsonObject {
    public static void main(String[] args) throws Exception {
        // Example SOAP XML Response with Empty Elements
        String soapXmlResponse = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Header>"
                + "<AuthToken>ABC123</AuthToken>"
                + "</soap:Header>"
                + "<soap:Body>"
                + "<GetUserResponse>"
                + "<User>"
                + "<ID>123</ID>"
                + "<Name>John Doe</Name>"
                + "<Age>30</Age>"
                + "<Active>true</Active>"
                + "<Balance>5000.75</Balance>"
                + "<Address>"
                + "<Street>Main St</Street>"
                + "<City></City>"  // Empty element should be ""
                + "</Address>"
                + "<Skills>"
                + "<Skill>Java</Skill>"
                + "<Skill>Python</Skill>"
                + "<Skill></Skill>" // Empty element should be ""
                + "</Skills>"
                + "<Notes></Notes>" // Empty element should be ""
                + "</User>"
                + "</GetUserResponse>"
                + "</soap:Body>"
                + "</soap:Envelope>";

        // Convert XML to JSONObject
        JSONObject jsonObject = convertXmlToJson(soapXmlResponse);

        // Pretty Print JSON
        System.out.println(jsonObject.toString(4));
    }

    public static JSONObject convertXmlToJson(String xml) throws Exception {
        // Parse XML into DOM
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new org.xml.sax.InputSource(new StringReader(xml)));

        // Convert the entire XML tree to JSONObject
        return wrapRootElement(document.getDocumentElement());
    }

    private static JSONObject wrapRootElement(Node node) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(node.getNodeName(), convertXmlNodeToJson(node));
        return jsonObject;
    }

    private static Object convertXmlNodeToJson(Node node) {
        NodeList nodeList = node.getChildNodes();
        JSONObject jsonObject = new JSONObject();
        boolean hasChildElements = false;

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node child = nodeList.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                hasChildElements = true;
                String key = child.getNodeName();
                Object value = convertXmlNodeToJson(child);

                if (child.hasChildNodes() && child.getChildNodes().getLength() == 1 && child.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                    String textContent = child.getTextContent().trim();
                    jsonObject.put(key, textContent); // Keep empty values as ""
                } else {
                    // Handle repeated elements (convert to JSONArray)
                    if (jsonObject.has(key)) {
                        Object existingValue = jsonObject.get(key);
                        JSONArray array;
                        if (existingValue instanceof JSONArray) {
                            array = (JSONArray) existingValue;
                        } else {
                            array = new JSONArray();
                            array.put(existingValue);
                        }
                        array.put(value);
                        jsonObject.put(key, array);
                    } else {
                        jsonObject.put(key, value);
                    }
                }
            }
        }

        // If no child elements exist and it's an empty node, return ""
        return hasChildElements ? jsonObject : "";
    }

    private static Object parseValue(String value) {
        // Check for null or empty string first
        if (value == null || value.trim().isEmpty()) {
            return "";  // Keep empty elements as empty strings
        }

        // Convert Boolean values
        if (value.equalsIgnoreCase("true")) {
            return true;
        } 
        if (value.equalsIgnoreCase("false")) {
            return false;
        }

        // Try to parse as a number (Integer or Double)
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value); // Convert to Double if it has a decimal
            } else {
                return Integer.parseInt(value); // Convert to Integer if it's a whole number
            }
        } catch (NumberFormatException e) {
            return value; // Keep as String if it's not a valid number
        }
    }

}