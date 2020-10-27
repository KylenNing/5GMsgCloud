package cc.htdf.msgcloud.datacenter.service.impl;

import cc.htdf.msgcloud.datacenter.domain.po.D5gAirPO;
import cc.htdf.msgcloud.datacenter.domain.po.D5gLifeIndexPO;
import cc.htdf.msgcloud.datacenter.domain.po.D5gObserveWeatherPO;
import cc.htdf.msgcloud.datacenter.mapper.D5gAirMapper;
import cc.htdf.msgcloud.datacenter.mapper.D5gLifeIndexMapper;
import cc.htdf.msgcloud.datacenter.mapper.D5gObserveWeatherMapper;
import cc.htdf.msgcloud.datacenter.service.ObserveDataService;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ningyq
 * @Date: 2020/9/9
 * @Description: TODO
 */
@Service
public class ObserveDataServiceImpl implements ObserveDataService {

    @Resource
    private D5gObserveWeatherMapper d5gObserveWeatherMapper;

    @Resource
    private D5gAirMapper d5gAirMapper;

    @Resource
    private D5gLifeIndexMapper d5gLifeIndexMapper;

    private final Map<String,String> ultravioletRaysIndexMap = ImmutableMap.of("1","弱","2","较弱",
            "3","中等","4","强","5","很强"
    );

    private final Map<String,String> coldIndexMap = ImmutableMap.of("1","室内外温差大，要及时增减衣物","2","寒风凛冽，注意防寒保暖",
            "3","寒潮袭来气温骤降，稍有不慎就会病倒"
    );

    private final Map<String,String> wearingIndexMap = new HashMap<String,String>(){{
        put("1","短衣短裤");put("2","轻薄衬衫");put("3","长袖棉衫");put("4","夹克衫");
        put("5","秋装、毛衣");put("6","棉服类衣物");put("7","冬季大衣");put("8","厚羽绒服");
    }};

    /**
     * @Description: 根据数据源和areacode获取观测数据
     * @param  @param
     * @author ningyq
     * @date 2020/9/11
     */
    @Override
    public D5gObserveWeatherPO getObserveDataByAreacode(Integer dataSourceArea, Integer areacode) {
        return d5gObserveWeatherMapper.getDataByDataSource(dataSourceArea,areacode);
    }

    /**
     * @Description: 根据数据源和areacode获取空气质量数据
     * @param  @param
     * @author ningyq
     * @date 2020/9/11
     */
    @Override
    public D5gAirPO getAirDataByAreacode(Integer dataSourceArea, Integer areacode) {
        return d5gAirMapper.getDataByDataSource(dataSourceArea,areacode);
    }

    /**
     * @Description: 根据数据源和areacode获取生活指数数据
     * @param  @param
     * @author ningyq
     * @date 2020/9/11
     */
    @Override
    public List<Map> getIndexDataByAreacode(Integer dataSourceArea, Integer areacode) {

        List<D5gLifeIndexPO> poList = d5gLifeIndexMapper.getLastIndexList(dataSourceArea,areacode);
        List<Map> resList = new ArrayList<>();
        for(D5gLifeIndexPO po : poList){
            String indexName = po.getIndexName();
            Map map = new HashMap();
            if(indexName.equals("紫外线指数")){
                map.put("indexName",indexName);
                map.put("indexLevel",ultravioletRaysIndexMap.get(po.getIndexLevel()));
                map.put("indexContent",po.getIndexContent());
            }else if(indexName.equals("感冒指数")){
                map.put("indexName",indexName);
                map.put("indexLevel",po.getIndexContent());
                map.put("indexContent",coldIndexMap.get(po.getIndexLevel()));
            }else if(indexName.equals("穿衣指数")){
                map.put("indexName",indexName);
                map.put("indexLevel",wearingIndexMap.get(po.getIndexLevel()));
                map.put("indexContent",po.getIndexContent());
            }else {
                map.put("indexName",indexName);
                map.put("indexLevel",po.getIndexContent());
                map.put("indexContent",po.getIndexContent());
            }
            resList.add(map);
        }
        return resList;
    }


}