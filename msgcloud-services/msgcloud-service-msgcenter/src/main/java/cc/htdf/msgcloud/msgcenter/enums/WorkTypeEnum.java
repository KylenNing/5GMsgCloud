package cc.htdf.msgcloud.msgcenter.enums;

/**
 * @Author: guozx
 * @Date: 2020/9/4
 * @Description:
 */
public enum WorkTypeEnum {

    doctor(1, "医生"),
    teacher(2, "教师"),
    nurse(3, "护士"),
    host(4, "主持人"),
    driver(5, "司机"),
    farmer(6, "农民"),
    writer(7, "作家"),
    waiter(8, "服务员"),
    worker(9, "工人"),
    singer(10, "歌手");

    // 成员变量
    private int key;
    private String value;

    // 构造方法
    WorkTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(int key) {
        for (WorkTypeEnum workTypeEnum : WorkTypeEnum.values()) {
            if (workTypeEnum.getKey() == key) {
                return workTypeEnum.value;
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
