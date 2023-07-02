package cloud.tanyanbo.xml;

import org.w3c.dom.Document;

public interface Parser {

  Document getDomFromXmlFile(String filePath);
}
