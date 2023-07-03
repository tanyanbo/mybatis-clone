package cloud.tanyanbo.xml;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MapperParserImpl extends ParserImpl implements MapperParser {

  @Override
  public SqlQuery parse(String id, Document dom) {
    try {
      XPathFactory xpathFactory = XPathFactory.newInstance();
      XPath xpath = xpathFactory.newXPath();
      String expression = String.format(
        "//select[@id='%1$s'] | //insert[@id='%1$s'] | //update[@id='%1$s'] | //delete[@id='%1$s']",
        id);
      NodeList nodeList = (NodeList) xpath.evaluate(expression, dom, XPathConstants.NODESET);
      Node node = nodeList.item(0);

      boolean isPreparedStatement = false;
      AllVariables variables = getVariables(node.getTextContent());
      for (Variable variable : variables.getVariables()) {
        if (variable.type() == VariableType.PARAMETER) {
          isPreparedStatement = true;
          break;
        }
      }

      return new SqlQuery(node.getTextContent(), isPreparedStatement,
        variables.getVariables());
    } catch (XPathExpressionException e) {
      e.printStackTrace();
    }
    return null;
  }
}
