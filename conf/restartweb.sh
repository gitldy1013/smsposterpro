#!/bin/bash
PROJECT_PATH=/usr/local/sms/
PROJECT_NAME=smsposterpro.jar
PROJECT_ALL_LOG_NAME=log/runLog.log
# stop process
tpid=`ps -ef|grep $PROJECT_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Stop Process...'
    kill -15 $tpid
fi
sleep 5

tpid=`ps -ef|grep $PROJECT_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Kill Process!'
    kill -9 $tpid
else
    echo 'Stop Success!'
fi

# start process
tpid=`ps -ef|grep $PROJECT_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'App is already running.'
else
    echo 'App is NOT running.'
    nohup java -Dfile.encoding=utf-8 -jar $PROJECT_PATH$PROJECT_NAME >/dev/null 2>&1 &
    echo Start Success!
    sleep 5
    tail -f -n 0 $PROJECT_PATH$PROJECT_ALL_LOG_NAME|while read line 
    do
       echo $line
       is_startup=`echo $line|grep "Startup complete ..."|wc -l`
       if [ $is_startup -eq 1 ];
           then kill -9 `ps axu|grep "tail -f -n 0 "$PROJECT_PATH$PROJECT_ALL_LOG_NAME|grep -v "grep"|awk '{printf $2}'`
        fi 
    done
fi
