package cloud.tanyanbo.sql;

import cloud.tanyanbo.xml.MapperParser;
import cloud.tanyanbo.xml.SqlQuery;
import cloud.tanyanbo.xml.Variable;
import cloud.tanyanbo.xml.VariableType;
import com.zaxxer.hikari.HikariDataSource;
import java.lang.reflect.Method;
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
  public <T> T query(Method method, Map<String, Object> params, Document dom,
    HikariDataSource dataSource) {
    SqlQuery query = mapperParser.parse(method.getName(), dom);
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
          List<Object> objects = parseResult(resultSet,
            method.getGenericReturnType().getTypeName());
          return objects.size() == 0 ? null
            : objects.size() == 1 ? (T) objects.get(0) : (T) objects;
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
      List<Object> objects = parseResult(resultSet, method.getGenericReturnType().getTypeName());
      return objects.size() == 0 ? null : objects.size() == 1 ? (T) objects.get(0) : (T) objects;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private <T> List<T> parseResult(ResultSet resultSet, String returnType) {
    try {
      if (isGenericReturnType(returnType)) {
        returnType = returnType.substring(returnType.indexOf("<") + 1, returnType.indexOf(">"));
      }
      Class<?> clazz = Class.forName(returnType);

      T instance = (T) clazz.getDeclaredConstructor().newInstance();

      List<T> result = new ArrayList<>();

      while (resultSet.next()) {
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); ++i) {
          String columnName = resultSet.getMetaData().getColumnName(i);
          columnName = toCamelCase(columnName);
          Object value = resultSet.getObject(i);
          clazz.getDeclaredField(columnName).set(instance, value);
        }
        result.add(instance);
      }

      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private boolean isGenericReturnType(String returnType) {
    return returnType.contains("<") && returnType.contains(">");
  }

  private static String toCamelCase(String s) {
    String[] parts = s.split("_");
    StringBuilder camelCaseString = new StringBuilder(parts[0]);
    for (int i = 1; i < parts.length; i++) {
      camelCaseString.append(toProperCase(parts[i]));
    }
    return camelCaseString.toString();
  }

  private static String toProperCase(String s) {
    return s.substring(0, 1).toUpperCase() +
      s.substring(1).toLowerCase();
  }
}
