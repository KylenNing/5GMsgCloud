package cc.htdf.msgcloud.msgcenter.enums;

/**
 * @Author: guozx
 * @Date: 2020/9/4
 * @Description:
 */
public enum SexTypeEnum {

    MALE(1, "男"),
    FEMALE(2, "女");

    // 成员变量
    private int key;
    private String value;

    // 构造方法
    SexTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(int key) {
        for (SexTypeEnum sexTypeEnum : SexTypeEnum.values()) {
            if (sexTypeEnum.getKey() == key) {
                return sexTypeEnum.value;
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
