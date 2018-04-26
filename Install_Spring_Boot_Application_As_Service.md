* Copy *_local-exec.jar, .conf and .properties file to a directory
* Change the values in .conf and .properties directory according to the environment
* Change the permission for all files: chmod u+rwx -R *
* Create symbolic link to init.d: sudo ln -s ~/Downloads/SpringBootApps/RestServiceAndConsumer_local-exec.jar /etc/init.d/RestServiceAndConsumer
* Start the service: service RestServiceAndConsumer start
* Available commands start | stop | restart | status