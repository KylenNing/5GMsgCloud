package cc.htdf.msgcloud.common.utils;

import com.google.common.io.Files;
import io.minio.*;
import io.minio.errors.*;
import lombok.Cleanup;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * author: JT
 * date: 2020/8/10
 * title:
 */
public class StorageUtil {

    private StorageUtil() {}

    private static Map<String, String> mediaTypeMap = new HashMap<String,String>(){{{
        put("mp3", "audio/mp3");
        put("mp4", "video/mp4");
        put("png", "imgs/png");
        put("gif", "imgs/gif");
        put("jpg", "imgs/jpeg");
        put("jpeg", "imgs/jpeg");
        put("json", "application/json");
    }}};

    public static String mediaType(String extName) {
        return mediaTypeMap.get(extName);
    }

    private static MinioClient minioClient;

    static {
        Yaml storageYaml = new Yaml();
        Map<String,Object> storageConfigMap = storageYaml.load(
                StorageUtil.class.getClassLoader().getResourceAsStream("storage.yml")
        );
        Map<String, Object> storageMap = (Map<String, Object>) storageConfigMap.get("storage");
        if (Objects.isNull(storageMap)) {
            throw new RuntimeException("未找到Storage相关配置!");
        }
        Map<String, Object> minioMap = (Map<String, Object>) storageMap.get("minio");
        if (Objects.isNull(minioMap)) {
            throw new RuntimeException("未找到Minio相关配置!");
        }

        String url = (String) minioMap.get("url");
        String accessKey = (String) minioMap.get("accessKey");
        String securtKey = (String) minioMap.get("securtKey");

        configMinio(url, accessKey, securtKey);

    }

    public static void configMinio(String url, String accessKey, String securtKey) {
        minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, securtKey)
                .build();
    }

    public static boolean checkBucketExists(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    public static void makeBucket(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, RegionConflictException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }

    public static void removeBucket(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        minioClient.removeBucket(
                RemoveBucketArgs.builder().bucket(bucketName).build()
        );
    }

    public static String uploadFile(
            String bucketName,
            String objectName,
            InputStream inputStream,
            String contentType
    ) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException {
        return uploadFile(bucketName, objectName, inputStream, contentType, null, null);
    }

    public static String uploadFile(
            String bucketName,
            InputStream inputStream,
            String extName,
            String contentType
    ) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException {
        if (!extName.startsWith(".")) {
            extName = "." + extName;
        }
        return uploadFile(bucketName, UUID.randomUUID()+ extName, inputStream, contentType, null, null);
    }

    public static String uploadFile(
            String bucketName,
            InputStream inputStream,
            String suffix
    ) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException {
        return uploadFile(bucketName, UUID.randomUUID() + "." + suffix, inputStream, null, null, null);
    }

    public static String uploadFile(String bucketName, File file, String contentType) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException {
        String suffix = Files.getFileExtension(file.getName());
        @Cleanup
        FileInputStream fileInputStream = new FileInputStream(file);
        return uploadFile(bucketName, UUID.randomUUID() + "." + suffix, fileInputStream, contentType, null, null);
    }

    public static String uploadFile(
            String bucketName,
            String objectName,
            InputStream inputStream,
            String contentType,
            Map<String, String> header,
            Map<String, String> userMetadata) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException, RegionConflictException {
        PutObjectArgs.Builder builder = PutObjectArgs.builder();
        builder.bucket(bucketName);
        builder.object(objectName);
        builder.stream(inputStream, inputStream.available(), -1);
        if (!Objects.isNull(contentType)) {
            builder.contentType(contentType);
        }
        if (!Objects.isNull(header)) {
            builder.headers(header);
        }
        if (!Objects.isNull(userMetadata)) {
            builder.userMetadata(userMetadata);
        }
        if (!checkBucketExists(bucketName)) {
            makeBucket(bucketName);
        }
        minioClient.putObject(builder.build());
        return objectName;
    }

    /**
     * 获取文件流
     * @param bucketName  存储桶名称
     * @param objName   文件名称
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws InvalidBucketNameException
     * @throws ErrorResponseException
     */
    public static InputStream getFileInputStream(String bucketName, String objName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        return minioClient.getObject(
                GetObjectArgs.builder().bucket(bucketName).object(objName).build()
        );
    }


    /**
     * 文件删除
     * @param bucketName
     * @param objName
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws InvalidBucketNameException
     * @throws ErrorResponseException
     */
    public static String remove(String bucketName, String objName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        minioClient.removeObject(
                RemoveObjectArgs.builder().bucket(bucketName).object(objName).build()
        );
        return objName;
    }


}
