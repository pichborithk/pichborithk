import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class SoapXmlToJsonNode {
    public static void main(String[] args) throws Exception {
        // Example SOAP XML Response
        String soapXmlResponse = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Body>"
                + "<GetUserResponse>"
                + "<User>"
                + "<ID>123</ID>"
                + "<Name>John Doe</Name>"
                + "<Age>30</Age>"
                + "<Active>true</Active>"
                + "<Balance>5000.75</Balance>"
                + "</User>"
                + "</GetUserResponse>"
                + "</soap:Body>"
                + "</soap:Envelope>";

        // Convert XML to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = convertSoapXmlToJson(soapXmlResponse, objectMapper);

        // Pretty Print JSON
        String jsonOutput = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        System.out.println(jsonOutput);
    }

    public static JsonNode convertSoapXmlToJson(String xml, ObjectMapper objectMapper) throws Exception {
        // Parse XML into DOM
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new org.xml.sax.InputSource(new StringReader(xml)));

        // Extract SOAP Body
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node bodyNode = (Node) xPath.evaluate("//*[local-name()='Body']/*", document, XPathConstants.NODE);

        // Convert XML Node to JSON
        return convertXmlNodeToJson(bodyNode, objectMapper);
    }

    private static JsonNode convertXmlNodeToJson(Node node, ObjectMapper objectMapper) {
        ObjectNode jsonObject = objectMapper.createObjectNode();
        if (node.hasChildNodes()) {
            NodeList nodeList = node.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node child = nodeList.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    String value = child.getTextContent().trim();
                    jsonObject.put(child.getNodeName(), value); // Keep all values as string
                }
            }
        }
        return jsonObject;
    }
}
