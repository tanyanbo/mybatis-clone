package cloud.tanyanbo.sql;

import com.zaxxer.hikari.HikariDataSource;
import org.w3c.dom.Document;

public interface Executor {

  <T> T query(String queryId, Object[] parameter, Document dom, HikariDataSource dataSource);
}
