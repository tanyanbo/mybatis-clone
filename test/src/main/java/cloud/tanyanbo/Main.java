package cloud.tanyanbo;

import cloud.tanyanbo.mapper.BrandMapper;
import cloud.tanyanbo.session.SqlSession;
import cloud.tanyanbo.session.SqlSessionFactory;
import cloud.tanyanbo.session.SqlSessionFactoryBuilder;
import java.net.URL;

public class Main {

  public static void main(String[] args) {
    System.setProperty("database-password", System.getenv("database-password"));
    URL resource = Main.class.getResource("/mybatis-config.xml");
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(
      resource.getPath(), "prod");

    SqlSession sqlSession = sqlSessionFactory.openSession();
    BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);
    mapper.deleteById(10);
  }
}
