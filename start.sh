#!/bin/bash

mvn clean
mvn install

# Test for PID file
if [ -f ~/logs/gilmore.pid ]; then
    gilmore_old_pid=$(cat ~/logs/gilmore.pid)

    # Test if PID is still running
    ps -p $gilmore_old_pid &> /dev/null
    if [ $? == 0 ]; then

        # Test operating system
        uname | grep 'MINGW' &> /dev/null
        if [ $? == 0 ]; then
            operating_system="Windows"
        fi

        uname | grep 'Linux' &> /dev/null
        if [ $? == 0 ]; then
            operating_system="Linux"
        fi

        # Test if PID is a java process
        if [ $operating_system == "Windows" ]; then
            ps -p $gilmore_old_pid | grep 'java' &> /dev/null
        elif [ $operating_system == "Linux" ]; then
            ps -p $gilmore_old_pid -o comm= | grep 'java' &> /dev/null
        fi

        if [ $? == 0 ]; then
            kill $gilmore_old_pid
            echo "[start.sh] Killed former gilmore process $gilmore_old_pid"
        fi
    fi
fi

java -cp target/gilmore-*-jar-with-dependencies.jar nl.codefox.gilmore.Gilmore &
echo $! > ~/logs/gilmore.pid
