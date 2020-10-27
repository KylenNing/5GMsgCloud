FROM anapsix/alpine-java

WORKDIR /app
ADD http://172.17.247.250/repo/msyh.ttf /usr/share/fonts/msyh.ttf
COPY target/msgcloud-core-executor-base.jar /app/msgcloud-core-executor-base.jar

RUN echo "https://mirrors.aliyun.com/alpine/v3.6/main/" > /etc/apk/repositories \
    && echo "https://mirrors.aliyun.com/alpine/v3.6/community/" >> /etc/apk/repositories \
    && apk update \
    && apk add tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && chmod a+r /usr/share/fonts/msyh.ttf \
    && apk add font-adobe-100dpi \
    && cd /usr/share/fonts/ \
    && fc-cache -f \
    && mkdir -p /data/gwc/forecast \
    && mkdir -p /app/log

CMD export LC_ALL=C.UTF-8 \
    && cd /app \
    && java -jar /app/msgcloud-core-executor-base.jar --spring.profiles.active=deploy -Dfile.encoding=UTF-8
