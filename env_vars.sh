#!/bin/bash

_env=prod
application_name=Observer.jar

# POM file details
pom=pom.xml
dev_pom=dev-pom.xml
prod_pom=prod-pom.xml

# application.properties file details
resources_dir="src/main/resources"
application_file_properties="${resources_dir}/application.properties"
application_file_default_properties="${resources_dir}/default-application.properties"

