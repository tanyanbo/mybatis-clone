package cloud.tanyanbo.xml;

import java.util.List;

public record SqlQuery(String query, boolean isPreparedStatement, List<Variable> params) {

}
