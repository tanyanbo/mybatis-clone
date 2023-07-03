package cloud.tanyanbo;

import cloud.tanyanbo.mapper.BrandMapper;
import cloud.tanyanbo.pojo.Brand;
import cloud.tanyanbo.session.SqlSession;
import cloud.tanyanbo.session.SqlSessionFactory;
import cloud.tanyanbo.session.SqlSessionFactoryBuilder;
import java.net.URL;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    URL resource = Main.class.getResource("/mybatis-config.xml");
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(
      resource.getPath(), "prod");

    SqlSession sqlSession = sqlSessionFactory.openSession();
    BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);
//    mapper.selectById(1);
    List<Brand> brands = mapper.selectAll();
  }
}
