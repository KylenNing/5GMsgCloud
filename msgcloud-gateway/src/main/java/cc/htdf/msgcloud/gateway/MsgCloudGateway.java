package cc.htdf.msgcloud.gateway;

import cc.htdf.msgcloud.db.annotation.EnableDB;
import cc.htdf.msgcloud.db.annotation.EnableMybatis;
import cc.htdf.msgcloud.db.domain.DBName;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * author: JT
 * date: 2020/8/18
 * title:
 */
@EnableDB(defaultDB = DBName.DB_MSGCLOUD, db = {DBName.DB_MSGCLOUD})
@EnableMybatis(basePackages = "cc.htdf.msgcloud.gateway.mapper")
@SpringBootApplication(scanBasePackages = "cc.htdf.msgcloud")
@EnableDiscoveryClient
public class MsgCloudGateway {

    public static void main(String[] args) {
        SpringApplication.run(MsgCloudGateway.class, args);
    }

}
