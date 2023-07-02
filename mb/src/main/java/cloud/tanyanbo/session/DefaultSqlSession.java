package cloud.tanyanbo.session;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class DefaultSqlSession implements SqlSession {

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getMapper(Class<T> clazz) {
    return (T) Proxy.newProxyInstance(
      clazz.getClassLoader(),
      new Class[]{clazz},
      (proxy, method, args) -> {
        System.out.printf("method: %s, args: %s", method.getName(),
          Arrays.toString(args));
        return null;
      }
    );
  }
}
