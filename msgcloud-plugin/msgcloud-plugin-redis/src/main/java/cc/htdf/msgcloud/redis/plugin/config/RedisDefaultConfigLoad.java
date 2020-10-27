package cc.htdf.msgcloud.redis.plugin.config;

import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.common.plugin.ConfigLoad;
import cc.htdf.msgcloud.redis.domain.RedisSource;
import cc.htdf.msgcloud.redis.exception.RedisConfigException;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * author: JT
 * date: 2020/8/7
 * title:
 */
public class RedisDefaultConfigLoad implements ConfigLoad<RedisSource> {

    private String profileActive;

    public RedisDefaultConfigLoad(String profieActive) {
        this.profileActive = profieActive;
    }

    @Override
    public Map<String, RedisSource> load() {
        String dbYmlPath = Objects.isNull(this.profileActive) ? "redis.yml" : String.format("redis-%s.yml", this.profileActive);
        Yaml yaml = new Yaml();
        Map<String, Object> redisDefaultConfigMap = yaml.load(
                this.getClass().getClassLoader().getResourceAsStream(dbYmlPath)
        );
        if (Objects.isNull(redisDefaultConfigMap)) {
            this.throwRedisConfigException(dbYmlPath);
        }
        Map<String, Object> msgcloudObj = Optional.ofNullable(redisDefaultConfigMap.get("msgcloud"))
                .map(obj -> (Map<String, Object>) obj).orElse(null);

        if (Objects.isNull(msgcloudObj)) {
            this.throwRedisConfigException(dbYmlPath);
        }
        List<Map<String, Object>> sourceMapList = Optional.of(msgcloudObj.get("redis"))
                .map(obj -> (List<Map<String, Object>>) obj)
                .orElse(null);
        if (Objects.isNull(sourceMapList)) {
            this.throwRedisConfigException(dbYmlPath);
        }
        return sourceMapList.stream()
                .map((Map<String, Object> sourceMap) -> {
                    RedisSource redisSource = new RedisSource();
                    redisSource.setName(String.valueOf(sourceMap.get("name")));
                    redisSource.setIsCluster(Boolean.valueOf(String.valueOf(sourceMap.get("isCluster"))));
                    List<String> nodeList = (List<String>)(sourceMap.get("nodes"));
                    redisSource.setNodes(nodeList);
                    redisSource.setMaxRedirects(Integer.valueOf(String.valueOf(sourceMap.get("max-redirects"))));
                    redisSource.setDatabase(Integer.valueOf(String.valueOf(sourceMap.get("database"))));
                    redisSource.setPassword(Optional.ofNullable(sourceMap.get("password")).map(String::valueOf).orElse(null));
                    Map<String, Object> poolMap = (Map<String, Object>) sourceMap.get("pool");
                    redisSource.setPoolMaxActive(Integer.valueOf(String.valueOf(poolMap.get("max-active"))));
                    redisSource.setPoolMaxIdle(Integer.valueOf(String.valueOf(poolMap.get("max-idle"))));
                    redisSource.setPoolMinIdle(Integer.valueOf(String.valueOf(poolMap.get("min-idle"))));
                    redisSource.setPoolMaxWait(Integer.valueOf(String.valueOf(poolMap.get("max-wait"))));
                    return redisSource;
                }).collect(Collectors.toMap(RedisSource::getName, source -> source));
    }

    public void throwRedisConfigException(String dbYmlPath) {
        throw new RedisConfigException(ExceptionCode.ERROR, "未正确配置配置文件[{}]", dbYmlPath);
    }

}
