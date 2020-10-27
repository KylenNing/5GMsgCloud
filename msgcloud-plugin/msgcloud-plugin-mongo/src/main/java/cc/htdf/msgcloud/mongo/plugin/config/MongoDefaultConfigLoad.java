package cc.htdf.msgcloud.mongo.plugin.config;

import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.common.plugin.ConfigLoad;
import cc.htdf.msgcloud.mongo.domain.MongoSource;
import cc.htdf.msgcloud.mongo.exception.MongoConfigException;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: JT
 * @date: 2019/11/26
 * @Title:
 */
public class MongoDefaultConfigLoad implements ConfigLoad<MongoSource> {

    private String profileActive;

    public MongoDefaultConfigLoad(String profieActive) {
        this.profileActive = profieActive;
    }

    @Override
    public Map<String, MongoSource> load() {
        String dbYmlPath = Objects.isNull(this.profileActive) ? "mongo.yml" : String.format("mongo-%s.yml", this.profileActive);
        Yaml yaml = new Yaml();
        Map<String, Object> mongoDefaultConfigMap = yaml.load(
                this.getClass().getClassLoader().getResourceAsStream(dbYmlPath)
        );
        if (Objects.isNull(mongoDefaultConfigMap))
            this.throwMongoConfigException(dbYmlPath);
        Map<String, Object> skydefenderObj = Optional.ofNullable(mongoDefaultConfigMap.get("skydefender"))
                .map(obj -> (Map<String, Object>) obj).orElse(null);
        if (Objects.isNull(skydefenderObj))
            this.throwMongoConfigException(dbYmlPath);
        List<Map<String, String>> datasourceMapList = Optional.of(skydefenderObj.get("mongodb"))
                .map(obj -> (List<Map<String, String>>) obj)
                .orElse(null);
        if (Objects.isNull(datasourceMapList))
            this.throwMongoConfigException(dbYmlPath);
        return datasourceMapList.stream()
                .map(datasourceMap -> {
                    MongoSource mongoSource = new MongoSource();
                    String name = datasourceMap.get("name");
                    String uri = datasourceMap.get("uri");
                    if (Objects.isNull(name) || Objects.isNull(uri))
                        this.throwMongoConfigException(dbYmlPath);
                    mongoSource.setName(name);
                    mongoSource.setUri(uri);
                    return mongoSource;
                }).collect(Collectors.toMap(
                        mongoSource -> mongoSource.getName(), mongoSource -> mongoSource
                ));
    }

    /**
     * 未正确配置配置文件异常
     * @param dbYmlPath
     */
    private void throwMongoConfigException(String dbYmlPath) {
        throw new MongoConfigException(ExceptionCode.ERROR, "未正确配置配置文件[{}]", dbYmlPath);
    }

}
