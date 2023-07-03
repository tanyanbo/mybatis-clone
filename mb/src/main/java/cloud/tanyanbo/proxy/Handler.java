package cloud.tanyanbo.proxy;

import cloud.tanyanbo.session.SqlSession;
import cloud.tanyanbo.xml.MapperParser;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Handler implements InvocationHandler {

  private final Class<?> clazz;
  private final MapperParser mapperParser;
  private final SqlSession sqlSession;

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    sqlSession.executeQuery(method.getName(), args, clazz);
    return null;
  }
}
