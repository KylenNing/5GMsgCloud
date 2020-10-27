package cc.htdf.msgcloud.msgcenter.enums;

public enum ButtonTypeEnum {

    build(1, "新建"),
    modify(2, "修改"),
    delete(3, "删除"),
    upload(4, "上传"),
    move(5, "移动"),
    generate(6, "生成"),
    approval(7, "审批"),
    send(8, "发送"),
    jurisdiction(9, "权限"),
    deleteMaterial(10, "删除素材"),
    deleteLabel(11, "删除标签"),
    off(12, "下架");

    // 成员变量
    private int key;
    private String value;

    // 构造方法
    ButtonTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(int key) {
        for (ButtonTypeEnum buttonTypeEnum : ButtonTypeEnum.values()) {
            if (buttonTypeEnum.getKey() == key) {
                return buttonTypeEnum.value;
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
