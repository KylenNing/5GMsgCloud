package cc.htdf.msgcloud.common.utils;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: ningyq
 * @Date: 2020/8/7
 * @Description: TODO
 */
public class HttpRequestUtils {


    public static InputStream getFileByUrl(String url) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        InputStream inputStream = null;
        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<byte[]>(headers),
                byte[].class);

        byte[] result = response.getBody();
        inputStream = new ByteArrayInputStream(result);
        return inputStream;
    }

    public static<T> HttpResponse post(String uri, T param) {
        return post(uri, param, null);
    }

    public static<T> HttpResponse post(String uri, T param, Map<String, String> headers)  {
        return post(uri, param, headers, null);
    }

    public static<T> HttpResponse post(String uri, T param, Map<String, String> headers, String textFormat)  {
        HttpRequest client = HttpRequest.post(uri);
        if (!Objects.isNull(headers) && !headers.isEmpty()) {
            client = client.addHeaders(headers);
        }

        String body = null;
        String contentType = null;
        if (Objects.isNull(textFormat)) {
            body = JSONObject.toJSONString(param);
            contentType = ContentType.JSON.toString();
        } else {
            switch (textFormat.toUpperCase()) {
                case "JSON":
                    body = JSONObject.toJSONString(param);
                    contentType = ContentType.JSON.toString();
                    break;
                case "XML":
                    body = String.valueOf(param);
                    contentType = ContentType.XML.toString();
                    break;
                default:
                    body = String.valueOf(param);
            }
        }
        if (Objects.isNull(contentType)) {
            client.body(body);
        } else {
            client.body(body, contentType);
        }
        return client.execute();
    }

    public static HttpResponse get(String uri) {
        return get(uri, null);
    }

    public static HttpResponse get(String uri, Map<String, Object> param) {
        return get(uri, param, null);
    }

    public static HttpResponse get(String uri, Map<String, Object> param, Map<String, String> headers) {
        if (!Objects.isNull(param) && !param.isEmpty()) {
            uri += "?";
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                uri = entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        HttpRequest client = HttpRequest.get(uri);
        if (!Objects.isNull(headers) && !headers.isEmpty()) {
            client = client.addHeaders(headers);
        }

        return client.execute();
    }




}