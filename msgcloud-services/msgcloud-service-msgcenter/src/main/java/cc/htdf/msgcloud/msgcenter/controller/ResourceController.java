package cc.htdf.msgcloud.msgcenter.controller;

import cc.htdf.msgcloud.common.utils.StorageUtil;
import com.google.common.io.Files;
import io.minio.errors.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * author: JT
 * date: 2020/8/10
 * title:
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {


    @GetMapping("/view/{bucketName}/{imageName}")
    public ResponseEntity view(@PathVariable String bucketName, @PathVariable String imageName) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "inline;filename=\"" + URLEncoder.encode(imageName) + "\"");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        InputStream inputStream = StorageUtil.getFileInputStream(bucketName, imageName);

        String extName = Files.getFileExtension(imageName);
        MediaType mediaType = MediaType.valueOf(StorageUtil.mediaType(extName));
        return ResponseEntity.ok()
                 .headers(headers)
                // .contentLength(imageGridFile.getLength())
                .contentType(mediaType)
                .body(new InputStreamResource(inputStream));
    }
}
