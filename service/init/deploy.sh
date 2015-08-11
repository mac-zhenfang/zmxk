#!/bin/bash

echo "###### stop tomcat ######"

`$CATALINA_HOME/bin/catalina.sh stop`

echo "backup the last build"
rm -rf /opt/builds/service-last-2
mkdir /opt/builds/service-last-2
mv /opt/builds/service-last /opt/builds/service-last-2
mv /opt/builds/service /opt/builds/service-last
mkdir /opt/builds/service
cp /opt/builds/current/service.war /opt/builds/service/
echo "###### unzip the service.war #####"
cd /opt/builds/service
echo " current folder " `pwd`
jar -xfv service.war
echo "##### replace the jdbc.properties #####"
rm -rf /opt/builds/service/WEB-INF/classes/jdbc.properties
cp -R /opt/builds/properties/* /opt/builds/service/WEB-INF/classes/
echo "##### delete the ROOT ######"
rm -rf $CATALINA_HOME/webapps/ROOT/
mkdir $CATALINA_HOME/webapps/ROOT
echo "##### copy the current build into "
cp -R * $CATALINA_HOME/webapps/ROOT/

echo "##### start the tomcat ######"
$CATALINA_HOME/bin/catalina.sh start