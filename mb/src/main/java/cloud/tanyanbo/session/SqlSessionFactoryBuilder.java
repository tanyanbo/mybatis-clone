package cloud.tanyanbo.session;

import cloud.tanyanbo.xml.ConfigParser;
import cloud.tanyanbo.xml.ConfigParserImpl;

public class SqlSessionFactoryBuilder {

  public SqlSessionFactory build(String configFilePath) {
    ConfigParser parser = new ConfigParserImpl();
    return new DefaultSqlSessionFactory(configFilePath, parser);
  }

  public SqlSessionFactory build(String configFilePath, String environmentId) {
    ConfigParser parser = new ConfigParserImpl();
    return new DefaultSqlSessionFactory(configFilePath, environmentId, parser);
  }
}
