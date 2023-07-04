package cloud.tanyanbo.session;

import java.util.Map;

public interface SqlSession {

  <T> T getMapper(Class<T> clazz);

  void close();

  <T> T executeQuery(String queryId, Map<String, Object> params, Class<?> mapperType);
}
