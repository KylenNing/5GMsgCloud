package cc.htdf.msgcloud.redis.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * author: JT
 * date: 2020/8/7
 * title:
 */
@Setter
@Getter
public class RedisSource {

    private String name;

    private Boolean isCluster;

    private List<String> nodes;

    private Integer maxRedirects;

    private Integer database;

    private String password;

    private Integer poolMaxActive;

    private Integer poolMaxIdle;

    private Integer poolMinIdle;

    private Integer poolMaxWait;

}
