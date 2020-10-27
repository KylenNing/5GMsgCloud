package cc.htdf.msgcloud.msgcenter.enums;

public enum ChoiceTypeEnum {

    Travel(1, "出行"),
    Tourism(2, "旅游"),
    Airing(3, "晾晒"),
    Physical(4, "锻炼"),
    Wash(5, "洗车");

    // 成员变量
    private int key;
    private String value;

    // 构造方法
    ChoiceTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(int key) {
        for (ChoiceTypeEnum choiceTypeEnum : ChoiceTypeEnum.values()) {
            if (choiceTypeEnum.getKey() == key) {
                return choiceTypeEnum.value;
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
