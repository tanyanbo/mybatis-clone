package cloud.tanyanbo.session;

import org.junit.jupiter.api.Test;

public class SessionTest {

  @Test
  public void testConnection() {
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(
      "test/src/main/resources/mybatis-config.xml");

    sqlSessionFactory.openSession();
  }
}
