#!/bin/bash
JARFile="../../target/PropertiesExample.jar"
PIDFile="../pid/PropertiesExampleProduction.pid"
PROPERTIESFile="../conf/production.properties"
LOGGINGConfFile="../conf/logback-spring.xml"
JVM_OPTS="-Xmx2g"
SPRING_OPTS="--spring.config.location=$PROPERTIESFile --logging.config=$LOGGINGConfFile --spring.pid.file=$PIDFile"

function check_if_process_is_running {
 PID=$(print_process)
 if [ '' ==  "$PID" ]
 then
    return 1
 fi
 if ps -p $PID > /dev/null
 then
     return 0
 else
     return 1
 fi
}

function print_process {
    if [ ! -f $PIDFile ]
    then
        echo ''
    else
        echo $(<"$PIDFile")
    fi
}

case "$1" in
  status)
    if check_if_process_is_running
    then
      echo $(print_process)" is running"
    else
      echo "Process not running: $(print_process)"
    fi
    ;;
  stop)
    if ! check_if_process_is_running
    then
      echo "Process $(print_process) already stopped"
      exit 0
    fi
    kill -TERM $(print_process)
    echo -ne "Waiting for process to stop"
    NOT_KILLED=1
    for i in {1..20}; do
      if check_if_process_is_running
      then
        echo -ne "."
        sleep 1
      else
        NOT_KILLED=0
      fi
    done
    echo
    if [ $NOT_KILLED = 1 ]
    then
      echo "Cannot kill process $(print_process)"
      exit 1
    fi
    echo "Process stopped"
    ;;
  start)
    if [ -f $PIDFile ] && check_if_process_is_running
    then
      echo "Process $(print_process) already running"
      exit 1
    fi
    nohup java $JVM_OPTS -jar $JARFile $SPRING_OPTS > /dev/null &
    echo "Process started"
    ;;
  restart)
    $0 stop
    if [ $? = 1 ]
    then
      exit 1
    fi
    $0 start
    ;;
  *)
    echo "Usage: $0 {start|stop|restart|status}"
    exit 1
esac

exit 0