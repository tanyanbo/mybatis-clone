package cloud.tanyanbo.xml;

public enum SqlQueryType {
  SELECT("select"),
  UPDATE("update"),
  DELETE("delete"),
  INSERT("insert");

  private final String type;

  SqlQueryType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
