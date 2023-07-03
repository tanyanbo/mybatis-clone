package cloud.tanyanbo.session;

import cloud.tanyanbo.proxy.InternalProxy;
import com.zaxxer.hikari.HikariDataSource;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultSqlSession implements SqlSession {

  HikariDataSource dataSource;

  @Override
  public void close() {
    dataSource.close();
  }

  @Override
  public <T> T getMapper(Class<T> clazz) {
    return InternalProxy.getMapper(clazz, this);
  }

  @Override
  public List<Object> selectList(String queryId) {
    return null;
  }

  @Override
  public Object selectOne(String queryId) {
    return null;
  }
}
