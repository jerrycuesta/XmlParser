
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMParserDemo {

    public static void main(String[] args) throws Exception {

        //Get the DOM Builder Factory
        DocumentBuilderFactory factory
                = DocumentBuilderFactory.newInstance();

        //Get the DOM Builder
        DocumentBuilder builder = factory.newDocumentBuilder();


        final String resourcesPath = "employees.xml";
        URL xmlUrl = DOMParserDemo.class.getResource(resourcesPath);
        String xmlText = new Scanner(DOMParserDemo.class.getResourceAsStream(resourcesPath), "UTF-8").useDelimiter("\\A").next();
        //ByteArrayInputStream
        InputStream stream = DOMParserDemo.class.getResourceAsStream(resourcesPath);

        Document document = builder.parse(stream);

        if (document == null) {
            System.out.println("Failed to Parse");
            return;
        }

        Element docElement = document.getDocumentElement();
        NodeList nodeList = document.getDocumentElement().getChildNodes();

        List<Employee> empList = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            // only process Element nodes
            if (!(node instanceof Element)) {
                continue;
            }

            Employee emp = new Employee();
            emp.id = node.getAttributes().
                    getNamedItem("id").getNodeValue();

            NodeList childNodes = node.getChildNodes();

            for (int j = 0; j < childNodes.getLength(); j++) {

                Node child = childNodes.item(j);

                //Identifying the child tag of employee encountered. 
                if (child instanceof Element) {

                    String content = child.getTextContent().trim();

                    switch (child.getNodeName()) {
                        case "firstName":
                            emp.firstName = content;
                            break;
                        case "lastName":
                            emp.lastName = content;
                            break;
                        case "location":
                            emp.location = content;
                            break;
                    }
                }
            }
            empList.add(emp);
        }

        //Printing the Employee list populated.
        for (Employee emp : empList) {
            System.out.println(emp);
        }
    }

}

class Employee {

    String id;
    String firstName;
    String lastName;
    String location;

    @Override
    public String toString() {
        return firstName + " " + lastName + "(" + id + ")" + location;
    }
}
