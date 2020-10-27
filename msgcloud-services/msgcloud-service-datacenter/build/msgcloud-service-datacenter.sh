IMAGE_NAME="msgcloud-service-datacenter"
CONTAINER_NAME="msgcloud-service-datacenter"
SCOPE="deploy"
docker build --build-arg SCOPE=$SCOPE -t $IMAGE_NAME/msgcloud:v1.0  ./
CID=$(docker ps -a | grep $CONTAINER_NAME | awk '{print $1}')
if [ -n "$CID" ]; then
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME
fi
docker run  -d --name $CONTAINER_NAME --dns 8.8.8.8 \
            --network msgcloud-network \
            --network-alias $IMAGE_NAME \
            --restart=always $IMAGE_NAME/msgcloud:v1.0
