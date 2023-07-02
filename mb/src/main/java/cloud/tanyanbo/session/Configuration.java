package cloud.tanyanbo.session;


import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Configuration {

  private List<Environment> environments = new ArrayList<>();

  private String currentEnvironmentId;

  public void addEnvironment(Environment environment) {
    environments.add(environment);
  }
}
