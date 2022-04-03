#!/bin/sh

# 时间戳
now_datetime=`date +%Y%m%d%H%M%S`

# version
app_version="1.0-SNAPSHOT"

# all service
service1="wanxinp2p-account-service"
service2="wanxinp2p-consumer-service"
service3="wanxinp2p-depository-agent-service"
service4="wanxinp2p-discover-server"
service5="wanxinp2p-gateway-server"
service6="wanxinp2p-repayment-service"
service7="wanxinp2p-transaction-service"
service8="wanxinp2p-uaa-service"
service12="wanxinp2p-content-search-service"


# 备份
cd /home/p2p/wanxinp2p
mkdir ./bak/${now_datetime}
mv -v ${service1}/*.jar ${service2}/*.jar ${service3}/*.jar ${service4}/*.jar ${service5}/*.jar ${service6}/*.jar ${service7}/*.jar ${service8}/*.jar ${service11}/*.jar ${service12}/*.jar -t ./bak/${now_datetime}/

# 复制 Jenkins 打包后的 jar 到 Docker
cp -v /var/lib/jenkins/workspace/wanxinp2p/${service1}/target/${service1}.jar ./${service1}/${service1}-${app_version}.jar

cp -v /var/lib/jenkins/workspace/wanxinp2p/${service2}/target/${service2}.jar ./${service2}/${service2}-${app_version}.jar

cp -v /var/lib/jenkins/workspace/wanxinp2p/${service3}/target/${service3}.jar ./${service3}/${service3}-${app_version}.jar

cp -v /var/lib/jenkins/workspace/wanxinp2p/${service4}/target/${service4}.jar ./${service4}/${service4}-${app_version}.jar

cp -v /var/lib/jenkins/workspace/wanxinp2p/${service5}/target/${service5}.jar ./${service5}/${service5}-${app_version}.jar

cp -v /var/lib/jenkins/workspace/wanxinp2p/${service6}/target/${service6}.jar ./${service6}/${service6}-${app_version}.jar

cp -v /var/lib/jenkins/workspace/wanxinp2p/${service7}/target/${service7}.jar ./${service7}/${service7}-${app_version}.jar

cp -v /var/lib/jenkins/workspace/wanxinp2p/${service8}/target/${service8}.jar ./${service8}/${service8}-${app_version}.jar

cp -v /var/lib/jenkins/workspace/wanxinp2p/${service11}/target/${service11}.jar ./${service11}/${service11}-${app_version}.jar

cp -v /var/lib/jenkins/workspace/wanxinp2p/${service12}/target/${service12}.jar ./${service12}/${service12}-${app_version}.jar

# 重新构建
/usr/local/bin/docker-compose build

# 启动
/usr/local/bin/docker-compose up -d