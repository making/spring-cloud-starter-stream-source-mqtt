#!/bin/sh

VERSION=`ruby -r rexml/document -e 'puts REXML::Document.new(File.new(ARGV.shift)).elements["/project/version"].text' pom.xml`
./mvnw clean install scs:generate-app -DskipTests=true
find target/scs -type f -print | xargs grep -l '<artifactId>spring-cloud-starter-stream-source-mqtt' | xargs sed -i '' "s/<artifactId>spring-cloud-starter-stream-source-mqtt/<version>$VERSION<\/version><artifactId>spring-cloud-starter-stream-source-mqtt/g"
./mvnw install -f target/scs
