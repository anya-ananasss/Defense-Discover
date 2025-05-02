package backend.academy.apigateway.service;


public interface RedisService {

    void setValue(String key, String value);
    String getValue(String key);
    void deleteKey(String key);
}