package cc.htdf.msgcloud.executor.base;

import cc.htdf.msgcloud.db.annotation.EnableDB;
import cc.htdf.msgcloud.db.annotation.EnableMybatis;
import cc.htdf.msgcloud.db.domain.DBName;
import cc.htdf.msgcloud.mongo.annotation.EnableMongodb;
import cc.htdf.msgcloud.mongo.config.MongoSourceName;
import cc.htdf.msgcloud.redis.annotation.EnableRedis;
import cc.htdf.msgcloud.redis.domain.RedisSourceName;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * author: JT
 * date: 2020/8/6
 * title:
 */
@EnableRedis(defaultSource = RedisSourceName.MSGCLOUD_CLUSTER, source = RedisSourceName.MSGCLOUD_CLUSTER)
@EnableMongodb(defaultSource = MongoSourceName.HTDF_SETS, sources = {MongoSourceName.HTDF_SETS})
@EnableDB(defaultDB = DBName.DB_MSGCLOUD, db = {DBName.DB_MSGCLOUD, DBName.DB_HTDF_OAUTH})
@EnableMybatis(basePackages = "cc.htdf.msgcloud.executor.base.mapper")
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class MsgCloudBaseExecutorApp {

    public static void main(String[] args) {
        SpringApplication.run(MsgCloudBaseExecutorApp.class, args);
    }
}
