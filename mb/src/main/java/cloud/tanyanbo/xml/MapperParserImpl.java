package cloud.tanyanbo.xml;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MapperParserImpl extends ParserImpl implements MapperParser {

  @Override
  public List<SqlQuery> getSqlQueries(Class<?> clazz) {
    String xmlFilePath = String.format("/%s.xml", clazz.getName().replace(".", "/"));
    URL xmlFileResource = clazz.getResource(xmlFilePath);

    List<SqlQuery> allQueries = new ArrayList<>();
    if (xmlFileResource == null) {
      return allQueries;
    }

    Document dom = getDomFromXmlFile(xmlFileResource.getPath());

    List<SqlQuery> selectStatements = getSqlQueriesByType(SqlQueryType.SELECT, dom);
    List<SqlQuery> updateStatements = getSqlQueriesByType(SqlQueryType.UPDATE, dom);
    List<SqlQuery> deleteStatements = getSqlQueriesByType(SqlQueryType.DELETE, dom);
    List<SqlQuery> insertStatements = getSqlQueriesByType(SqlQueryType.INSERT, dom);

    CollectionUtils.addAll(allQueries, selectStatements);
    CollectionUtils.addAll(allQueries, updateStatements);
    CollectionUtils.addAll(allQueries, deleteStatements);
    CollectionUtils.addAll(allQueries, insertStatements);

    return allQueries;
  }

  private List<SqlQuery> getSqlQueriesByType(SqlQueryType type, Document dom) {
    List<SqlQuery> queries = new ArrayList<>();
    NodeList queryNodes = dom.getElementsByTagName(type.getType());

    if (queryNodes == null || queryNodes.getLength() == 0) {
      return queries;
    }

    for (int i = 0; i < queryNodes.getLength(); i++) {
      queries.add(getSqlQueryFromNode(queryNodes.item(i), type));
    }
    return queries;
  }

  private SqlQuery getSqlQueryFromNode(Node item, SqlQueryType type) {
    return new SqlQuery(type, item.getTextContent(), false, null);
  }
}
