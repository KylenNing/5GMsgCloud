package cc.htdf.msgcloud.msgcenter.domain.dto;

import lombok.Data;

@Data
public class MsgReceiptCallbackInfo {

    private String address;

    private String messageId;

    private String deliveryStatus;

    private String description;
}
