package cloud.tanyanbo.sql;

import cloud.tanyanbo.xml.MapperParser;
import cloud.tanyanbo.xml.SqlQuery;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.w3c.dom.Document;

public class ExecutorImpl implements Executor {

  MapperParser mapperParser;

  public ExecutorImpl(MapperParser mapperParser) {
    this.mapperParser = mapperParser;
  }

  @Override
  public <T> T query(String queryId, Object[] parameter, Document dom,
    HikariDataSource dataSource) {
    SqlQuery query = mapperParser.parse(queryId, dom);

    if (query.isPreparedStatement()) {
      try (
        Connection connection = dataSource.getConnection();
      ) {
        PreparedStatement preparedStatement = connection.prepareStatement(query.query());
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return null;
    }

    try (
      Connection connection = dataSource.getConnection();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(query.query());
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
