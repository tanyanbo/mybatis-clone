package cloud.tanyanbo.pojo;

public class Brand {

  // id 主键
  public Integer id;
  // 品牌名称
  public String brandName;
  // 企业名称
  public String companyName;
  // 排序字段
  public Integer ordered;
  // 描述信息
  public String description;
  // 状态：0：禁用  1：启用
  public Integer status;

  @Override
  public String toString() {
    return "Brand{" +
      "id=" + id +
      ", brandName='" + brandName + '\'' +
      ", companyName='" + companyName + '\'' +
      ", ordered=" + ordered +
      ", description='" + description + '\'' +
      ", status=" + status +
      '}';
  }
}
