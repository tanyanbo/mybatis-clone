package cloud.tanyanbo.mapper;

import cloud.tanyanbo.pojo.Brand;
import java.util.List;

public interface BrandMapper {

  /**
   * 查询所有
   */
  List<Brand> selectAll();


  /**
   * 查看详情：根据Id查询
   */
  Brand selectById(int id);
}
