package cloud.tanyanbo.proxy;


import cloud.tanyanbo.session.SqlSession;
import cloud.tanyanbo.xml.MapperParserImpl;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class InternalProxy {

  private static final Map<Class<?>, Object> map = new HashMap<>();

  @SuppressWarnings("unchecked")
  public static <T> T getMapper(Class<T> clazz, SqlSession sqlSession) {
    T t = (T) map.get(clazz);

    if (t == null) {
      t = (T) Proxy.newProxyInstance(
        clazz.getClassLoader(),
        new Class[]{clazz},
        new Handler(clazz, new MapperParserImpl(), sqlSession)
      );
      map.put(clazz, t);
    }

    return t;
  }
}
