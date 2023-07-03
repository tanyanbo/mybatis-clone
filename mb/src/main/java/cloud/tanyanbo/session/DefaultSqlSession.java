package cloud.tanyanbo.session;

import cloud.tanyanbo.proxy.InternalProxy;
import cloud.tanyanbo.xml.SqlQuery;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
  public List<Object> selectList(SqlQuery query) {
    return null;
  }

  @Override
  public Object selectOne(SqlQuery query) {
    String sql = query.query();
    try (
      Connection connection = dataSource.getConnection();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
    ) {
      while (resultSet.next()) {
        System.out.println(resultSet.getString("brand_name"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
