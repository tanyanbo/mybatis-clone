package cloud.tanyanbo.xml;

import java.util.List;

public record SqlQuery(SqlQueryType type, String query, boolean isPreparedStatement,
                       List<String> variables) {

}
