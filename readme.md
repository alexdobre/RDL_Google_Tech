The RDL main web application  
============================

Build Instructions

Follw this step in installing merchant-sdk and paypal-core in local repository.
https://github.com/paypal/merchant-sdk-java


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


To fix a deploying error in JBOSS AS
cd .../jboss-as-7.1.1.Final/standalone/configuration
vi standalone.xml
"enable-welcome-root="false" instead of "true".



http://www.davidghedini.com/pg/entry/install_jboss_7_on_centos
By default, JBoss 7.1.1 is bound to the loopback IP of 127.0.0.1, so if we want to make it available on the web, we need to change this.

Locate standalone.xml under /usr/share/jboss-as/standalone/configuration/.

Open standalone.xml in vi or a text editor and look for the public interfaces node as shown below.

<interface name="public">
<inet-address value="${jboss.bind.address:127.0.0.1}"/>
</interface>

To make JBoss publicly accessible, change 127.0.0.1 to either 0.0.0.0 to allow access on all interfaces or to your public IP.



please view open issues and contribute

 https://code.google.com/p/google-guice/wiki/ServletModule
 http://blog.palominolabs.com/2011/08/15/a-simple-java-web-stack-with-guice-jetty-jersey-and-jackson/
 https://code.google.com/p/google-web-toolkit-incubator/wiki/LoginSecurityFAQ
 http://www.adrianwalker.org/2011/03/gwt-file-upload-with-event-based.html
 https://bitbucket.org/joscarsson/gwt-gaemultiupload-example/src/877e2d2c496b1a9f6c89ddbc433f64125047e3f4/src/gaemultiupload?at=master
 git pull -s recursive -X theirs origin master /


