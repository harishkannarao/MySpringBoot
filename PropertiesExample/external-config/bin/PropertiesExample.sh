#!/bin/bash

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  SCRIPT_DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$SCRIPT_DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
SCRIPT_DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

JARFile="$SCRIPT_DIR/../../target/PropertiesExample.jar"
PIDFile="$SCRIPT_DIR/../pid/PropertiesExampleProduction.pid"
PROPERTIESFile="$SCRIPT_DIR/../conf/production.yml"
LOGGINGConfFile="$SCRIPT_DIR/../conf/logback-spring.xml"
LOGGINGFileLocation="$SCRIPT_DIR/../log/PropertiesExample"
JVM_OPTS="-Xmx2g"
SPRING_OPTS="--spring.config.location=$PROPERTIESFile --logging.config=$LOGGINGConfFile --logging.myapp.file.location=$LOGGINGFileLocation --spring.pid.file=$PIDFile"

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
    if [ -n "$2" ]
    then
        JARFile=$2
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
    echo "Usage: $0 {start|stop|restart|status} [jarFile]"
    echo "Example of start with default jar: $0 start"
    echo "Example of start with overidden jar: $0 start \"/opt/PropertiesExample-2.0.jar\""
    echo "Example of Stop: $0 stop"
    exit 1
esac

exit 0