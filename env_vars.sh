#!/bin/bash

_env=dev
application_name=Observer.jar

# environment files location
env_dir=environment

# POM file details
pom=pom.xml
dev_pom="${env_dir}/dev-pom.xml"
prod_pom="${env_dir}/prod-pom.xml"

# Project specific files
test_data=deploy-test.sh
dev_run_script=runApp.sh
run_script_orig=runApp.sh-orig

# application.properties file details
resources_dir="src/main/resources"
application_file_properties="${resources_dir}/application.properties"
application_file_default_properties="${env_dir}/default-application.properties"
application_file_original_properties="${env_dir}/original-application.properties"

files=("dev-pom.xml" "prod-pom.xml")
prod_index=1
