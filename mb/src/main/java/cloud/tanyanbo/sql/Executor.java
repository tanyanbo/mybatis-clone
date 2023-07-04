package cloud.tanyanbo.sql;

import com.zaxxer.hikari.HikariDataSource;
import java.lang.reflect.Method;
import java.util.Map;
import org.w3c.dom.Document;

public interface Executor {

  <T> T query(Method method, Map<String, Object> params, Document dom,
    HikariDataSource dataSource);
}
