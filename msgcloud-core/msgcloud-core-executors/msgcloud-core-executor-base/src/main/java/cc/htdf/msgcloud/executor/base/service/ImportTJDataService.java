package cc.htdf.msgcloud.executor.base.service;

import java.text.ParseException;

/**
 * @Author: ningyq
 * @Date: 2020/9/9
 * @Description: TODO
 */
public interface ImportTJDataService {

    void importHourData() throws ParseException;

    void importDayData() throws ParseException;

    void importObserveData() throws ParseException;

    void importAirData() throws ParseException;

    void importLifeIndexData() throws ParseException;

    //public void updateAreaTable();

    void clearHistoryData();
}
