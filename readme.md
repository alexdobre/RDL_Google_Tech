The RDL main web application  
============================

Build Instructions  


Test in local stack  

1. in com.therdl.shared.Constants.java  
set public static final boolean DEPLOY = false;  
2 compile  
mvn clean package  
3. for production server testing  
mvn jetty:run  
4. local development dev url  
http://localhost:8080/  
  
Run on Application Server JBOSS  
1. in com.therdl.shared.Constants.java  
 set public static final boolean DEPLOY = true;  
2. compile  
 mvn clean package  
it should build the therdl.war file in “~/RDL_Google_Tech/target” directory  
then set up in your application server  

3. deployed url  
url http://<your host>:<your port if not 80>/therdl  




please view open issues and contribute

 https://code.google.com/p/google-guice/wiki/ServletModule
 http://blog.palominolabs.com/2011/08/15/a-simple-java-web-stack-with-guice-jetty-jersey-and-jackson/
 https://code.google.com/p/google-web-toolkit-incubator/wiki/LoginSecurityFAQ
 http://www.adrianwalker.org/2011/03/gwt-file-upload-with-event-based.html
 https://bitbucket.org/joscarsson/gwt-gaemultiupload-example/src/877e2d2c496b1a9f6c89ddbc433f64125047e3f4/src/gaemultiupload?at=master
 git pull -s recursive -X theirs origin master /
