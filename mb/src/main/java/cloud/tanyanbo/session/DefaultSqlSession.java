package cloud.tanyanbo.session;

import cloud.tanyanbo.proxy.InternalProxy;
import cloud.tanyanbo.xml.MapperParser;
import cloud.tanyanbo.xml.SqlQuery;
import com.zaxxer.hikari.HikariDataSource;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;

public class DefaultSqlSession implements SqlSession {

  HikariDataSource dataSource;
  MapperParser mapperParser;
  Map<Class<?>, Document> mapperToDomMap = new HashMap<>();

  public DefaultSqlSession(HikariDataSource dataSource, MapperParser mapperParser) {
    this.dataSource = dataSource;
    this.mapperParser = mapperParser;
  }

  @Override
  public void close() {
    dataSource.close();
  }

  @Override
  public <T> T getMapper(Class<T> clazz) {
    return InternalProxy.getMapper(clazz, this);
  }

  @Override
  public List<Object> selectList(SqlQuery query) {
    return null;
  }

  @Override
  public Object selectOne(String name, Object[] params, Class<?> clazz) {
    if (mapperToDomMap.get(clazz) == null) {
      URL resource = clazz.getResource("/" + clazz.getName().replace(".", "/") + ".xml");
      if (resource == null) {
        throw new RuntimeException("mapper xml file not found");
      }
      mapperToDomMap.put(clazz, mapperParser.getDomFromXmlFile(resource.getPath()));
    }

    SqlQuery query = mapperParser.parse(name, mapperToDomMap.get(clazz));
    try (
      Connection connection = dataSource.getConnection();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(query.query());
    ) {
      while (resultSet.next()) {
        System.out.println(resultSet.getString("brand_name"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
