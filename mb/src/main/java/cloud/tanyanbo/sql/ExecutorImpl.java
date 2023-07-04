package cloud.tanyanbo.sql;

import cloud.tanyanbo.xml.MapperParser;
import cloud.tanyanbo.xml.SqlQuery;
import cloud.tanyanbo.xml.Variable;
import cloud.tanyanbo.xml.VariableType;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;

public class ExecutorImpl implements Executor {

  MapperParser mapperParser;

  public ExecutorImpl(MapperParser mapperParser) {
    this.mapperParser = mapperParser;
  }

  @Override
  public <T> T query(String queryId, Map<String, Object> params, Document dom,
    HikariDataSource dataSource) {
    SqlQuery query = mapperParser.parse(queryId, dom);
    String sqlStatement = query.query();

    if (query.isPreparedStatement()) {
      // replace all raw string with actual values and replace all variable with ?
      List<String> variableNames = new ArrayList<>();
      for (int i = 0; i < query.params().size(); ++i) {
        Variable variable = query.params().get(i);
        if (variable.type() == VariableType.RAW_STRING) {
          sqlStatement = sqlStatement.replace(variable.fullPattern(),
            params.get(variable.value()).toString());
        } else {
          sqlStatement = sqlStatement.replace(variable.fullPattern(), "?");
          variableNames.add(variable.value());
        }
      }

      try (
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)
      ) {
        for (int i = 0; i < variableNames.size(); ++i) {
          preparedStatement.setObject(i + 1, params.get(variableNames.get(i)));
        }

        boolean hasResultSet = preparedStatement.execute();
        if (hasResultSet) {
          ResultSet resultSet = preparedStatement.getResultSet();
          while (resultSet.next()) {
            System.out.println(resultSet.getString("brand_name"));
          }
        } else {
          return null;
        }
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
