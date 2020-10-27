package cc.htdf.msgcloud.db.plugin.config;

import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.common.plugin.ConfigLoad;
import cc.htdf.msgcloud.db.domain.DB;
import cc.htdf.msgcloud.db.exception.DBConfigException;
import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * author: JT
 * date: 2019/11/14
 * Title:
 */
@Setter
@Getter
public class DBConfigDefaultLoad implements ConfigLoad<DB> {

    private String profileActive;

    public DBConfigDefaultLoad(String profileActive) {
        this.profileActive = profileActive;
    }

    @Override
    public Map<String, DB> load() {
        String dbYmlPath = Objects.isNull(this.profileActive) ? "db.yml" : String.format("db-%s.yml", this.profileActive);
        Yaml yaml = new Yaml();
        Map<String, Object> dbDefaultConfigMap = yaml.load(
                this.getClass().getClassLoader().getResourceAsStream(dbYmlPath)
        );
        if (Objects.isNull(dbDefaultConfigMap))
            this.throwDBConfigException(dbYmlPath);
        Map<String, Object> skydefenderObj = Optional.ofNullable(dbDefaultConfigMap.get("skydefender"))
                .map(obj -> (Map<String, Object>) obj).orElse(null);
        if (Objects.isNull(skydefenderObj))
            this.throwDBConfigException(dbYmlPath);
        List<Map<String, String>> datasourceMapList = Optional.of(skydefenderObj.get("datasource"))
                .map(obj -> (List<Map<String, String>>) obj)
                .orElse(null);
        if (Objects.isNull(datasourceMapList))
            this.throwDBConfigException(dbYmlPath);
        return datasourceMapList.stream()
                .map(datasourceMap -> {
                    DB dataSource = new DB();
                    dataSource.setName(datasourceMap.get("name"));
                    dataSource.setUrl(datasourceMap.get("url"));
                    dataSource.setDriverClassName(datasourceMap.get("driverClassName"));
                    dataSource.setUsername(datasourceMap.get("username"));
                    dataSource.setPassword(datasourceMap.get("password"));
                    return dataSource;
                }).collect(Collectors.toMap(
                        datasource -> datasource.getName(), dataSource -> dataSource
                ));
    }

    /**
     * 抛出DB配置异常
     * @param ymlpath yml文件路径
     */
    private void throwDBConfigException(String ymlpath) {
        throw new DBConfigException(ExceptionCode.ERROR, "未正确配置配置文件[{0}]", ymlpath);

    }
}
