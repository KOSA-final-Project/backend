#!/bin/bash

cd /home/ec2-user/app

DOCKER_APP_NAME=spring

# 실행중인 blue가 있는지
EXIST_BLUE=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

# green이 실행중이면 blue up
if [ -z "$EXIST_BLUE" ]; then
        echo "blue up"
        sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

        BEFORE_COLOR="green"
        AFTER_COLOR="blue"
        BEFORE_PORT=8082
        AFTER_PORT=8081

# blue가 실행중이면 green up
else
        echo "green up"
        sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

        BEFORE_COLOR="blue"
        AFTER_COLOR="green"
        BEFORE_PORT=8081
        AFTER_PORT=8082
fi

echo "${AFTER_COLOR} server up(port:${AFTER_PORT})"

# 2
for cnt in {1..10}
do
        echo "서버 응답 확인중(${cnt}/10)";
        UP=$(curl -s http://127.0.0.1:${AFTER_PORT}/health-check)
        if [ "${UP}" != "up" ]
                then
                        sleep 10
                        continue
                else
                        break
        fi
done

if [ $cnt -eq 10 ]
then
        echo "서버가 정상적으로 구동되지 않았습니다."
        exit 1
fi

# 3
sudo sed -i "s/${BEFORE_PORT}/${AFTER_PORT}/" /etc/nginx/conf.d/service-url.inc
sudo nginx -s reload
echo "배포완료."

# 4 - 이전 컨테이너 종료
echo "$BEFORE_COLOR server down(port:${BEFORE_PORT})"
echo "CPU 사용률 안정화 대기 중..."
sleep 10

RETRY_COUNT=0
MAX_RETRIES=3

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    echo "$BEFORE_COLOR 컨테이너 종료 시도 중... ($RETRY_COUNT/$MAX_RETRIES)"
    sudo docker-compose -p ${DOCKER_APP_NAME}-${BEFORE_COLOR} -f docker-compose.${BEFORE_COLOR}.yml down --force
    if [ $? -eq 0 ]; then
        echo "$BEFORE_COLOR 컨테이너가 정상적으로 중지되었습니다."
        break
    else
        echo "$BEFORE_COLOR 컨테이너를 중지하는 데 실패했습니다. 재시도합니다."
        RETRY_COUNT=$((RETRY_COUNT + 1))
        sleep 5
    fi
done

if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
    echo "$BEFORE_COLOR 컨테이너를 종료할 수 없습니다. 강제 종료를 시도합니다."
    sudo docker kill $(sudo docker ps -q --filter "name=${DOCKER_APP_NAME}-${BEFORE_COLOR}")
    if [ $? -ne 0 ]; then
        echo "강제 종료도 실패했습니다. Docker 데몬 상태를 확인하세요."
        exit 1
    fi
fi