FROM anapsix/alpine-java

WORKDIR /app
ADD http://172.17.247.250/repo/msyh.ttf /usr/share/fonts/msyh.ttf
COPY target/msgcloud-service-datacenter.jar /app/msgcloud-service-datacenter.jar

RUN echo "https://mirrors.aliyun.com/alpine/v3.6/main/" > /etc/apk/repositories \
    && echo "https://mirrors.aliyun.com/alpine/v3.6/community/" >> /etc/apk/repositories \
    && echo "Asia/Shanghai" > /etc/timezone \
    && chmod a+r /usr/share/fonts/msyh.ttf \
    && apk update \
    && apk add font-adobe-100dpi \
    && cd /usr/share/fonts/ \
    && fc-cache -f

CMD export LC_ALL=C.UTF-8 \
    && cd /app \
    && java -jar /app/msgcloud-service-datacenter.jar --spring.profiles.active=deploy -Dfile.encoding=UTF-8
