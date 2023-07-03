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

  protected AllVariables getVariables(String input) {
    Pattern pattern = Pattern.compile("(#|\\$)\\{([A-Za-z0-9_]*)}");
    Matcher matcher = pattern.matcher(input);

    AllVariables allVariables = new AllVariables();
    List<Variable> variables = allVariables.getVariables();
    while (matcher.find()) {
      variables.add(
        new Variable(
          matcher.group(1).equals("$") ? VariableType.RAW_STRING : VariableType.PARAMETER,
          matcher.group(2),
          matcher.group(0)));
      allVariables.setVariables(variables);
    }

    return allVariables;
  }
}
