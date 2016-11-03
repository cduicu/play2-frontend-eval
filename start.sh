#!/bin/sh

JAVA_HOME=/nfs/tools/jdk/linux/jdk1.8.0_25-x64
BASE_START_CMD="bin/buildhub -Dconfig.file=./conf/application.conf"
bash -c "nohup $BASE_START_CMD -Dhttp.port=9000 > nohup.log 2>&1&"
