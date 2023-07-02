package cloud.tanyanbo.xml;

import cloud.tanyanbo.session.Configuration;
import org.w3c.dom.Document;

public interface ConfigParser extends Parser {

  Configuration getConfigurationFromDom(Document doc);

}
