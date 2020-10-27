FROM msgcloud-ffmpeg/msgcloud:v1.0

WORKDIR /app
COPY target/msgcloud-service-msgcenter.jar /app/msgcloud-service-msgcenter.jar


CMD cd /app \
    && java -jar /app/msgcloud-service-msgcenter.jar --spring.profiles.active=deploy -Dfile.encoding=UTF-8
