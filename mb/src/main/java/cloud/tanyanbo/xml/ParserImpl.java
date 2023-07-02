package cloud.tanyanbo.xml;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public class ParserImpl implements Parser {

  @Override
  public Document getDomFromXmlFile(String filePath) {
    try {
      File inputFile = new File(filePath);

      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(inputFile);

      doc.getDocumentElement().normalize();

      return doc;
    } catch (Exception e) {
      throw new IllegalArgumentException("Error parsing xml file: " + filePath, e);
    }
  }

  protected AllProperties getPropertyNameIfIsProperty(String input) {
    Pattern pattern = Pattern.compile("(#|\\$)\\{([A-Za-z0-9_]*)}");
    Matcher matcher = pattern.matcher(input);

    AllProperties allProperties = new AllProperties();
    List<Property> properties = allProperties.getProperties();
    while (matcher.find()) {
      properties.add(
        new Property(
          matcher.group(1).equals("$") ? PropertyType.RAW_STRING : PropertyType.VARIABLE,
          matcher.group(2),
          matcher.group(0)));
      allProperties.setProperties(properties);
    }

    return allProperties;
  }
}
