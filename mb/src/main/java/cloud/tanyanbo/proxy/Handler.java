package cloud.tanyanbo.proxy;

import cloud.tanyanbo.xml.MapperParser;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Handler implements InvocationHandler {

  private final Class<?> clazz;
  private final MapperParser mapperParser;

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Arrays.stream(clazz.getDeclaredMethods())
      .forEach(m -> System.out.println(m.getName()));
    System.out.printf("method: %s, args: %s", method.getName(),
      java.util.Arrays.toString(args));
    return null;
  }
}
