package cloud.tanyanbo.proxy;


import cloud.tanyanbo.xml.MapperParserImpl;
import cloud.tanyanbo.xml.SqlQuery;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InternalProxy {

  private static final Map<Class<?>, Object> map = new HashMap<>();

  @SuppressWarnings("unchecked")
  public static <T> T getMapper(Class<T> clazz) {
    T t = (T) map.get(clazz);

    if (t == null) {
      t = (T) Proxy.newProxyInstance(
        clazz.getClassLoader(),
        new Class[]{clazz},
        (proxy, method, args) -> {
          MapperParserImpl mapperParser = new MapperParserImpl();
          List<SqlQuery> sqlQueries = mapperParser.getSqlQueries(clazz);
          System.out.println(sqlQueries);
          System.out.printf("method: %s, args: %s", method.getName(),
            java.util.Arrays.toString(args));
          return null;
        }
      );
      map.put(clazz, t);
    }

    return t;
  }
}
