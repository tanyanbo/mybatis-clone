package cloud.tanyanbo.xml;

import java.util.List;

public interface MapperParser {

  List<SqlQuery> getSqlQueries(Class<?> clazz);
}
