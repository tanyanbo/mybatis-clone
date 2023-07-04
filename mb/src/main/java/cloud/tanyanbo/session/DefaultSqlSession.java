package cloud.tanyanbo.session;

import cloud.tanyanbo.proxy.InternalProxy;
import cloud.tanyanbo.sql.Executor;
import cloud.tanyanbo.xml.MapperParser;
import com.zaxxer.hikari.HikariDataSource;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;

public class DefaultSqlSession implements SqlSession {

  HikariDataSource dataSource;
  MapperParser mapperParser;
  Map<Class<?>, Document> mapperToDomMap = new HashMap<>();
  Executor executor;

  public DefaultSqlSession(HikariDataSource dataSource, MapperParser mapperParser,
    Executor executor) {
    this.dataSource = dataSource;
    this.mapperParser = mapperParser;
    this.executor = executor;
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
  public <T> T executeQuery(String queryId, Map<String, Object> params, Class<?> mapperType) {
    Document domFromMapper = getDomFromMapper(mapperType);
    executor.query(queryId, params, domFromMapper, dataSource);
    return null;
  }

  private Document getDomFromMapper(Class<?> mapperType) {
    if (mapperToDomMap.get(mapperType) == null) {
      URL resource = mapperType.getResource("/" + mapperType.getName().replace(".", "/") + ".xml");
      if (resource == null) {
        throw new RuntimeException("mapper xml file not found");
      }
      mapperToDomMap.put(mapperType, mapperParser.getDomFromXmlFile(resource.getPath()));
    }
    return mapperToDomMap.get(mapperType);
  }
}
