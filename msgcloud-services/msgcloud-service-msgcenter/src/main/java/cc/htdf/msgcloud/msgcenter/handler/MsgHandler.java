package cc.htdf.msgcloud.msgcenter.handler;

import cc.htdf.msgcloud.common.domain.Msg;
import com.feinno.msgctenter.sdk.dto.AuditNotify;
import com.feinno.msgctenter.sdk.dto.StatusNotify;

/**
 * author: JT
 * date: 2020/8/14
 * title:
 */
public interface MsgHandler {

    void onMessageStateCallback(StatusNotify var1);

    void onMessageUpNotify(Msg var1);

    void onTemplateAuditResultNotify(AuditNotify var1);

    void onMaterialAuditResultNotify(AuditNotify var1);
}
