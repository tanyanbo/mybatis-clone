package cloud.tanyanbo.session;

import java.util.List;

public interface SqlSession {

  <T> T getMapper(Class<T> clazz);

  void close();

  List<Object> selectList(String queryId);

  Object selectOne(String queryId);
}
