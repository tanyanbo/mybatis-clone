package cloud.tanyanbo.xml;

import org.w3c.dom.Document;

public interface MapperParser extends Parser {

  SqlQuery parse(String id, Document dom);
}
