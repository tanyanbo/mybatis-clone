package cloud.tanyanbo.proxy;

import cloud.tanyanbo.session.SqlSession;
import cloud.tanyanbo.xml.MapperParser;
import cloud.tanyanbo.xml.SqlQuery;
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
    System.out.printf("method: %s, args: %s", method.getName(),
      java.util.Arrays.toString(args));
    SqlQuery query = mapperParser.parse(method.getName(), args);
    if (method.getName().equals("selectOne")) {
      sqlSession.selectOne(method.getName());
    }
    return null;
  }
}
