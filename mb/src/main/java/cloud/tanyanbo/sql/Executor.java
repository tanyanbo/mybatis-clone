package cloud.tanyanbo.sql;

import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import org.w3c.dom.Document;

public interface Executor {

  <T> T query(String queryId, Map<String, Object> params, Document dom,
    HikariDataSource dataSource);
}
