package cloud.tanyanbo.xml;

import java.io.File;
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

  protected Property getPropertyNameIfIsProperty(String input) {
    Pattern pattern = Pattern.compile("(#|\\$)\\{(.*)}");
    Matcher matcher = pattern.matcher(input);

    if (!matcher.find()) {
      return null;
    }

    return new Property(
      matcher.group(1).equals("$") ? PropertyType.RAW_STRING : PropertyType.VARIABLE,
      matcher.group(2)
    );
  }
}
