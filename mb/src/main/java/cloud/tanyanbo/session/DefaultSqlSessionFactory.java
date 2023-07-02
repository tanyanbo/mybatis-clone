package cloud.tanyanbo.session;

import cloud.tanyanbo.xml.ConfigParser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.w3c.dom.Document;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

  Configuration configuration;

  public DefaultSqlSessionFactory(String configFilePath, ConfigParser parser) {
    Document dom = parser.getDomFromXmlFile(configFilePath);
    configuration = parser.getConfigurationFromDom(dom);
  }

  public DefaultSqlSessionFactory(String configFilePath, String environmentId,
    ConfigParser parser) {
    this(configFilePath, parser);
    configuration.setCurrentEnvironmentId(environmentId);
  }

  @Override
  public SqlSession openSession() {
    DataSource dataSource = configuration.getEnvironments().stream()
      .filter(env -> env.id().equals(configuration.getCurrentEnvironmentId()))
      .findFirst()
      .orElseThrow()
      .dataSource();

    String url = dataSource.url();
    String username = dataSource.username();
    String password = dataSource.password();

    try (Connection connection = DriverManager.getConnection(url, username, password)) {
      Statement statement = connection.createStatement();
      String sql = "SELECT * FROM tb_brand";
      ResultSet resultSet = statement.executeQuery(sql);
      while (resultSet.next()) {
        System.out.println(resultSet.getString("brand_name"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return new DefaultSqlSession();
  }
}
