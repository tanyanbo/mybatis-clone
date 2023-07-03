package cloud.tanyanbo.session;

import cloud.tanyanbo.sql.ExecutorImpl;
import cloud.tanyanbo.xml.ConfigParser;
import cloud.tanyanbo.xml.MapperParser;
import cloud.tanyanbo.xml.MapperParserImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

@Slf4j
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

    HikariDataSource hikariDataSource = initHikariDataSource(dataSource);
    MapperParser mapperParser = new MapperParserImpl();

    return new DefaultSqlSession(
      hikariDataSource,
      mapperParser,
      new ExecutorImpl(mapperParser)
    );
  }

  private HikariDataSource initHikariDataSource(DataSource dataSource) {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(dataSource.url());
    config.setUsername(dataSource.username());
    config.setPassword(dataSource.password());
    config.setMaximumPoolSize(4);

    return new HikariDataSource(config);
  }
}
