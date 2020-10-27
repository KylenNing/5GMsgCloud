IMAGE_NAME="msgcloud-core-message"
CONTAINER_NAME="msgcloud-core-message"
SCOPE="deploy"
docker build --build-arg SCOPE=$SCOPE -t $IMAGE_NAME/msgcloud:v1.0  ./
CID=$(docker ps -a | grep $CONTAINER_NAME | awk '{print $1}')
if [ -n "$CID" ]; then
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME
fi
docker run  -d --name $CONTAINER_NAME --dns 8.8.8.8 \
            -p 8033:8033 \
            -v /data/log/$CONTAINER_NAME:/app/log \
            --add-host 5gmc.fetiononline.com:161.189.118.121 \
            --network msgcloud-network \
            --network-alias $IMAGE_NAME \
            --restart=always $IMAGE_NAME/msgcloud:v1.0
