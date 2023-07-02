package cloud.tanyanbo.session;

public interface SqlSession {

  <T> T getMapper(Class<T> clazz);
}
