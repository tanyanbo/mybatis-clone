package cloud.tanyanbo.session;

public interface SqlSession {

  <T> T getMapper(Class<T> clazz);

  void close();

  <T> T executeQuery(String queryId, Object[] params, Class<?> mapperType);
}
