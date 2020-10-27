package cc.htdf.msgcloud.datacenter;

import cc.htdf.msgcloud.db.annotation.EnableDB;
import cc.htdf.msgcloud.db.annotation.EnableMybatis;
import cc.htdf.msgcloud.db.domain.DBName;
import cc.htdf.msgcloud.redis.annotation.EnableRedis;
import cc.htdf.msgcloud.redis.domain.RedisSourceName;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * author: JT
 * date: 2020/8/5
 * title:
 */
@EnableRedis(source = {RedisSourceName.MSGCLOUD_CLUSTER, RedisSourceName.MSGCLOUD_REDIS}, defaultSource = RedisSourceName.MSGCLOUD_CLUSTER)
@EnableDB(defaultDB = DBName.DB_MSGCLOUD, db = {DBName.DB_MSGCLOUD, DBName.DB_HTDF_OAUTH})
@EnableMybatis(basePackages = "cc.htdf.msgcloud.datacenter.mapper")
@SpringBootApplication(scanBasePackages = "cc.htdf.msgcloud", exclude = {RedisAutoConfiguration.class})
@EnableDiscoveryClient
public class MsgCloudDataCenterApp {

    public static void main(String[] args) {
        SpringApplication.run(MsgCloudDataCenterApp.class, args);
    }
}
