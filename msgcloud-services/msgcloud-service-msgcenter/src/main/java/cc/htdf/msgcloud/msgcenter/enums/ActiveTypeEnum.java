package cc.htdf.msgcloud.msgcenter.enums;

/**
 * @Author: guozx
 * @Date: 2020/9/4
 * @Description:
 */
public enum ActiveTypeEnum {

    HIGH(1, "高"),
    MIDDLE(2, "中"),
    LOW(3, "低");

    // 成员变量
    private int key;
    private String value;

    // 构造方法
    ActiveTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(int key) {
        for (ActiveTypeEnum activeTypeEnum : ActiveTypeEnum.values()) {
            if (activeTypeEnum.getKey() == key) {
                return activeTypeEnum.value;
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
