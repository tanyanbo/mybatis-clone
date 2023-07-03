package cloud.tanyanbo.session;

import cloud.tanyanbo.xml.SqlQuery;
import java.util.List;

public interface SqlSession {

  <T> T getMapper(Class<T> clazz);

  void close();

  List<Object> selectList(SqlQuery query);

  Object selectOne(SqlQuery query);
}
