package cc.htdf.msgcloud.executor.base.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ningyq
 * @Date: 2020/9/10
 * @Description: TODO
 */
public class AreaCodeTransformUtils {

    public static List<Integer> tjAreaList = Arrays.asList(101030100,101030200,101030300,
            101030400,101030500,101030600,101030700,101030900,101031000,101031100,101031400);

    public static List<Integer> commonTjAreaList = Arrays.asList(
            120000,120100,120101,120102,120103,120104,120105,120106,120110,120111,
            120112,120113,120114,120115,120116,120221,120223,120225
    );

    public static Integer getLocalAreaCode(Integer code){

        Map<Integer,Integer> codeMap = new HashMap<Integer,Integer>(){{
            put(101030100,120100);
            put(101030200,120114);
            put(101030300,120115);
            put(101030400,120110);
            put(101030500,120111);
            put(101030600,120113);
            put(101030700,120221);
            put(101030900,120223);
            put(101031000,120112);
            put(101031100,120116);
            put(101031400,120225);

        }};
        return codeMap.get(code);
    }
}