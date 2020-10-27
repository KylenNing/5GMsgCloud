package cc.htdf.msgcloud.msgcenter.enums;

/**
 * @Author: guozx
 * @Date: 2020/9/4
 * @Description:
 */
public enum TravelTypeEnum {

    CAR(1, "汽车自驾"),
    BUS(2, "公共交通"),
    WALK(3, "步行通勤");

    // 成员变量
    private int key;
    private String value;

    // 构造方法
    TravelTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(int key) {
        for (TravelTypeEnum travelTypeEnum : TravelTypeEnum.values()) {
            if (travelTypeEnum.getKey() == key) {
                return travelTypeEnum.value;
            }
        }
        return null;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
