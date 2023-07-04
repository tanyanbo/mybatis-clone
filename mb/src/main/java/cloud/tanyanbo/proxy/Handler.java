package cloud.tanyanbo.proxy;

import cloud.tanyanbo.session.SqlSession;
import cloud.tanyanbo.xml.MapperParser;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Handler implements InvocationHandler {

  private final Class<?> clazz;
  private final MapperParser mapperParser;
  private final SqlSession sqlSession;

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Map<String, Object> params = new HashMap<>();
    for (int i = 0; i < method.getParameters().length; ++i) {
      params.put(method.getParameters()[i].getName(), args[i]);
    }
    return sqlSession.executeQuery(method, params, clazz);
  }
}
