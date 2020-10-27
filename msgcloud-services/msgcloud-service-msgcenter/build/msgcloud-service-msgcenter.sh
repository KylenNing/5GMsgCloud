IMAGE_NAME="msgcloud-service-msgcenter"
CONTAINER_NAME="msgcloud-service-msgcenter"
SCOPE="deploy"
docker build --build-arg SCOPE=$SCOPE -t $IMAGE_NAME/msgcloud:v1.0  ./
CID=$(docker ps -a | grep $CONTAINER_NAME | awk '{print $1}')
if [ -n "$CID" ]; then
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME
fi
docker run  -d --name $CONTAINER_NAME --dns 8.8.4.4 \
            -p 8032:8032 \
            -v /data/log/$CONTAINER_NAME:/app/log \
            --network msgcloud-network \
            --network-alias $IMAGE_NAME \
            --restart=always $IMAGE_NAME/msgcloud:v1.0
