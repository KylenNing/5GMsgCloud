package cc.htdf.msgcloud.executor.base.job;

import cc.htdf.msgcloud.executor.base.BaseTest;
import cc.htdf.msgcloud.executor.base.service.impl.ImportTJDataServiceImpl;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @Author: ningyq
 * @Date: 2020/9/9
 * @Description: TODO
 */
public class ImportTJData extends BaseTest {

    @Resource
    private ImportTJHourDataToMysqlJob importTJHourDataToMysqlJob;

    @Resource
    private ImportTJDayDataToMysqlJob importTJDayDataToMysqlJob;

    @Resource
    private ImportTJObserveDataToMysqlJob importTJObserveDataToMysqlJob;

    @Resource
    private ClearMysqlHistoryJob clearMysqlHistoryJob;

    @Resource
    private ImportTJAirDataToMysqlJob importTJAirDataToMysqlJob;

    @Resource
    private ImportTJLifeIndexDataToMysqlJob importTJLifeIndexDataToMysqlJob;

    @Resource
    private ImportTJDataServiceImpl importTJDataService;

    @Test
    public void hourDataTest() throws Exception {
        importTJHourDataToMysqlJob.execute("");
    }

    @Test
    public void dayDataTest() throws Exception {
        //importTJDayDataToMysqlJob.execute("");
        importTJDataService.importHourData();
    }

    @Test
    public void observeDataTest() throws Exception {
        importTJDataService.importObserveData();
    }

    @Test
    public void airDataTest() throws Exception {
        importTJAirDataToMysqlJob.execute("");
    }

    @Test
    public void indexDataTest() throws Exception {
        importTJLifeIndexDataToMysqlJob.execute("");
    }
//    @Test
//    public void updateAreaTable() throws Exception {
//        importTJDataService.updateAreaTable();
//    }

    @Test
    public void clearHistory() throws Exception {
        clearMysqlHistoryJob.execute("");
    }
}