package cloud.tanyanbo.session;

import java.lang.reflect.Method;
import java.util.Map;

public interface SqlSession {

  <T> T getMapper(Class<T> clazz);

  void close();

  <T> T executeQuery(Method method, Map<String, Object> params, Class<?> mapperType);
}
