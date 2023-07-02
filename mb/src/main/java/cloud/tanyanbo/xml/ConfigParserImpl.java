package cloud.tanyanbo.xml;

import cloud.tanyanbo.session.Configuration;
import cloud.tanyanbo.session.DataSource;
import cloud.tanyanbo.session.Environment;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigParserImpl extends ParserImpl implements ConfigParser {

  @Override
  public Configuration getConfigurationFromDom(Document doc) {
    Configuration configuration = new Configuration();

    Node environmentsNode = doc.getElementsByTagName("environments").item(0);
    Element environmentsElement = (Element) environmentsNode;
    String defaultEnvironmentId = environmentsElement.getAttribute("default");
    configuration.setCurrentEnvironmentId(defaultEnvironmentId);

    NodeList environments = environmentsNode.getChildNodes();
    for (int i = 0; i < environments.getLength(); i++) {
      Node environment = environments.item(i);
      Environment env = getEnvironmentFromEnvironmentNode(environment);
      if (env != null) {
        configuration.addEnvironment(env);
      }
    }

    return configuration;
  }


  private Environment getEnvironmentFromEnvironmentNode(Node environment) {
    if (environment.getNodeType() != Node.ELEMENT_NODE) {
      return null;
    }

    Element element = (Element) environment;
    String id = element.getAttribute("id");

    DataSource dataSource = getDataSourceFromEnvironmentNode(environment);

    return dataSource == null ? null : new Environment(id, dataSource);
  }

  private DataSource getDataSourceFromEnvironmentNode(Node environment) {
    DataSource dataSource = null;

    for (int j = 0; j < environment.getChildNodes().getLength(); j++) {
      Node child = environment.getChildNodes().item(j);
      if (child.getNodeType() != Node.ELEMENT_NODE) {
        continue;
      }

      Element childElement = (Element) child;
      if (child.getNodeName().equals("dataSource")) {
        String type = childElement.getAttribute("type");
        NodeList properties = childElement.getElementsByTagName("property");

        String driver = "", url = "", username = "", password = "";
        for (int k = 0; k < properties.getLength(); ++k) {
          Node property = properties.item(k);
          Element propertyElement = (Element) property;

          String value = propertyElement.getAttribute("value");
          if (isProperty(value)) {
            value = System.getProperty(value.substring(2, value.length() - 1));
          }

          switch (propertyElement.getAttribute("name")) {
            case "driver" -> driver = value;
            case "url" -> url = value;
            case "username" -> username = value;
            case "password" -> password = value;
            default -> throw new IllegalArgumentException(
              "Unknown property name: " + propertyElement.getAttribute("name"));
          }
        }

        dataSource = new DataSource(type, driver, url, username, password);
      }
    }

    return dataSource;
  }

  private boolean isProperty(String input) {
    Pattern pattern = Pattern.compile("\\$\\{.*}");
    Matcher matcher = pattern.matcher(input);

    boolean found = matcher.find();
    matcher.reset();
    return found;
  }
}