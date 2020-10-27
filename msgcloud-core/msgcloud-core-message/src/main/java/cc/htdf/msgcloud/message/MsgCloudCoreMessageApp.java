package cc.htdf.msgcloud.message;

import cc.htdf.msgcloud.db.annotation.EnableDB;
import cc.htdf.msgcloud.db.annotation.EnableMybatis;
import cc.htdf.msgcloud.db.domain.DBName;
import cc.htdf.msgcloud.redis.annotation.EnableRedis;
import cc.htdf.msgcloud.redis.domain.RedisSourceName;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * author: JT
 * date: 2020/8/8
 * title:
 */
@EnableDB(db = DBName.DB_MSGCLOUD, defaultDB = DBName.DB_MSGCLOUD)
@EnableMybatis(basePackages = "cc.htdf.msgcloud.message.mapper")
@EnableRedis(source = {RedisSourceName.MSGCLOUD_CLUSTER}, defaultSource = RedisSourceName.MSGCLOUD_CLUSTER)
@SpringBootApplication(scanBasePackages = "cc.htdf.msgcloud")
public class MsgCloudCoreMessageApp {

    public static void main(String[] args) {
        SpringApplication.run(MsgCloudCoreMessageApp.class, args);
    }
}
