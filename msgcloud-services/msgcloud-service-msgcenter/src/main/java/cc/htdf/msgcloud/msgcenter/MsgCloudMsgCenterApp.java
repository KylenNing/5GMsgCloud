package cc.htdf.msgcloud.msgcenter;

import cc.htdf.msgcloud.db.annotation.EnableDB;
import cc.htdf.msgcloud.db.annotation.EnableMybatis;
import cc.htdf.msgcloud.db.domain.DBName;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * author: JT
 * date: 2020/8/5
 * title:
 */
@EnableAsync
@EnableFeignClients(basePackages = "cc.htdf.msgcloud.msgcenter.rest")
@EnableDB(defaultDB = DBName.DB_MSGCLOUD, db = {DBName.DB_MSGCLOUD, DBName.DB_HTDF_OAUTH, DBName.DB_HTDF_MSGCLOUD_TEST})
@EnableMybatis(basePackages = "cc.htdf.msgcloud.msgcenter.mapper")
@SpringBootApplication(scanBasePackages = "cc.htdf.msgcloud")
@EnableDiscoveryClient
public class MsgCloudMsgCenterApp {

    public static void main(String[] args) {
        SpringApplication.run(MsgCloudMsgCenterApp.class, args);
    }

}
