#!/bin/bash
BASEDIR=$(dirname $0)
PROPERTIES_DIR=$BASEDIR/properties
SERVICE_DIR=$BASEDIR/../target/service-0.0.1-SNAPSHOT

echo "###### stop tomcat ######"

$CATALINA_HOME/bin/shutdown.sh

echo "backup the last build"
rm -rf /opt/builds/service-last-2
mkdir /opt/builds/service-last-2
mv /opt/builds/service-last /opt/builds/service-last-2
mv /opt/builds/service /opt/builds/service-last
mkdir /opt/builds/service
cp -R $SERVICE_DIR/* /opt/builds/service/

echo "##### replace the jdbc.properties #####"
rm -rf /opt/builds/service/WEB-INF/classes/jdbc.properties
cp -R $PROPERTIES_DIR/* /opt/builds/service/WEB-INF/classes/
echo "##### delete the ROOT ######"
rm -rf $CATALINA_HOME/webapps/ROOT/
mkdir $CATALINA_HOME/webapps/ROOT
echo "##### copy the current build into "
cp -R /opt/builds/service/* $CATALINA_HOME/webapps/ROOT/

echo "##### start the tomcat ######"
$CATALINA_HOME/bin/catalina.sh start