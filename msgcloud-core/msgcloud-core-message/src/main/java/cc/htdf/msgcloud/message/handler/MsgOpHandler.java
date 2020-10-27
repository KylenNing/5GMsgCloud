package cc.htdf.msgcloud.message.handler;

import cc.htdf.msgcloud.common.domain.Msg;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 */
public interface MsgOpHandler {

    Msg generateMsg(Msg msg) throws ParseException, IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException, InvalidBucketNameException, InsufficientDataException, RegionConflictException;

}
