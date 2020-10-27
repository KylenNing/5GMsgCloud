package cc.htdf.msgcloud.common.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NumberUtils {

	/**
	 * 获取小数点后面几位
	 * @param value 值
	 * @param num 几位小数
	 * @return
	 */
	public static String toFix(String value,int num) {
		String z = "";
		for(int i=0;i<num;i++) {
			z += "0";
		}
		DecimalFormat decimalFormat=new DecimalFormat(z.length()==0?"0":("0."+z));//构造方法的字符格式这里如果小数不足num位,会以0补足.
		return decimalFormat.format(Float.parseFloat(value));//format 返回的是字符串
	}

	    public static String[] parseConditionCode(String conditionCode) {
	        int n = Integer.valueOf(conditionCode);
	        StringBuffer sb = new StringBuffer();
	        for (int i = 31; i >= 0; i--) {
	            if ((n & (1 << i)) != 0) {
	                sb.append("1");
	            } else {
	                sb.append("0");
	            }
	            if ((32 - i) % 8 == 0) {
	                sb.append(" ");
	            }
	        }
	        return sb.toString().split(" ");
	}


	public static void main(String[] args) {
		String[] l = parseConditionCode("39");
		for(String s:l) {
			System.out.println(Integer.parseInt(s,2));
		}
	}
	/**
	 * 获取小数点后面几位
	 * @param value 值
	 * @param num 几位小数
	 * @return
	 */
	public static String toFix(float value,int num) {
		String z = "";
		for(int i=0;i<num;i++) {
			z += "0";
		}
		DecimalFormat decimalFormat=new DecimalFormat(z.length()==0?"0":("0."+z));//构造方法的字符格式这里如果小数不足num位,会以0补足.
		return decimalFormat.format(value);//format 返回的是字符串
	}

	/**
	 * 获取小数点后面几位
	 * @param value 值
	 * @param num 几位小数
	 * @return
	 */
	public static float toFixFloat(float value,int num) {
		String z = "";
		for(int i=0;i<num;i++) {
			z += "0";
		}
		DecimalFormat decimalFormat=new DecimalFormat(z.length()==0?"0":("0."+z));//构造方法的字符格式这里如果小数不足num位,会以0补足.
		return Float.parseFloat(decimalFormat.format(value));//format 返回的是字符串
	}

	/**
	 * 华氏温度转摄氏温度
	 * @param tem  华氏温度
	 * @return
	 */
	public static String toC(String tem) {
		return toFix((Double.parseDouble(tem)-32)/1.8+"",1);
	}

	/**
	 * 英里每小时转米每秒
	 * @param winv  风速（英里每小时）
	 * @return
	 */
	/*public static String toMM(String winv) {
		return toFix(Double.parseDouble(winv)*0.44704+"",1);
	}*/

	/**
	 * km/h转m/s
	 * @param winv  风速（英里每小时）
	 * @return
	 */
	public static String toMM(String winv) {
		return toFix(Double.parseDouble(winv)*0.2777778+"",1);
	}

	/**
	 * 返回固定7个经纬度数据
	 */
	public static List<Map<String,Object>> getLLData(){
		List<Map<String,Object>> list = new ArrayList();
		Map<String,Object> map = new HashMap();
		map.put("UPLEFTPOINT","//");//左上角
		map.put("UPRIGHTPOINT", "//");//右上角
		map.put("BOTTOMLEFTPOINT", "//");//左下角
		map.put("BOTTOMRIGHTPOINT", "//");//右下角
		map.put("LON", "117.83");
		map.put("LAT", "31.58");
		list.add(map);

		Map<String,Object> map1 = new HashMap();
		map1.put("UPLEFTPOINT","//");//左上角
		map1.put("UPRIGHTPOINT", "//");//右上角
		map1.put("BOTTOMLEFTPOINT", "//");//左下角
		map1.put("BOTTOMRIGHTPOINT", "//");//右下角
		map1.put("LON", "117.28");
		map1.put("LAT", "31.27");
		list.add(map1);

		Map<String,Object> map2 = new HashMap();
		map2.put("UPLEFTPOINT","//");//左上角
		map2.put("UPRIGHTPOINT", "//");//右上角
		map2.put("BOTTOMLEFTPOINT", "//");//左下角
		map2.put("BOTTOMRIGHTPOINT", "//");//右下角
		map2.put("LON", "118.65");
		map2.put("LAT", "43.53");
		list.add(map2);

		Map<String,Object> map3 = new HashMap();
		map3.put("UPLEFTPOINT","//");//左上角
		map3.put("UPRIGHTPOINT", "//");//右上角
		map3.put("BOTTOMLEFTPOINT", "//");//左下角
		map3.put("BOTTOMRIGHTPOINT", "//");//右下角
		map3.put("LON", "127.35");
		map3.put("LAT", "46.08");
		list.add(map3);

		Map<String,Object> map4 = new HashMap();
		map4.put("UPLEFTPOINT","//");//左上角
		map4.put("UPRIGHTPOINT", "//");//右上角
		map4.put("BOTTOMLEFTPOINT", "//");//左下角
		map4.put("BOTTOMRIGHTPOINT", "//");//右下角
		map4.put("LON", "124.3");
		map4.put("LAT", "43.35");
		list.add(map4);

		Map<String,Object> map5 = new HashMap();
		map5.put("UPLEFTPOINT","//");//左上角
		map5.put("UPRIGHTPOINT", "//");//右上角
		map5.put("BOTTOMLEFTPOINT", "//");//左下角
		map5.put("BOTTOMRIGHTPOINT", "//");//右下角
		map5.put("LON", "117.87");
		map5.put("LAT", "37.17");
		list.add(map5);

		Map<String,Object> map6 = new HashMap();
		map6.put("UPLEFTPOINT","//");//左上角
		map6.put("UPRIGHTPOINT", "//");//右上角
		map6.put("BOTTOMLEFTPOINT", "//");//左下角
		map6.put("BOTTOMRIGHTPOINT", "//");//右下角
		map6.put("LON", "123.18");
		map6.put("LAT", "47.33");
		list.add(map6);

		return list;
	}
}
