package cloud.tanyanbo.session;

import cloud.tanyanbo.proxy.InternalProxy;

public class DefaultSqlSession implements SqlSession {

  @Override
  public <T> T getMapper(Class<T> clazz) {
    return InternalProxy.getMapper(clazz);
  }
}
