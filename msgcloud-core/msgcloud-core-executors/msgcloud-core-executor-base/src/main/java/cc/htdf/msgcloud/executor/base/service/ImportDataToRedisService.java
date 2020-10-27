package cc.htdf.msgcloud.executor.base.service;

import java.io.IOException;
import java.text.ParseException;

/**
 * @Author: ningyq
 * @Date: 2020/8/7
 * @Description: TODO
 */
public interface ImportDataToRedisService {

    public void importObserveDataToRedis() throws ParseException;

    public void importForcastDataToRedis() throws ParseException, IOException;
}
