#!/bin/bash

source env_vars.sh

delete_data_directories() {
	echo "Deleting data/error directory"
	rm -rf "data/error"
	echo "Done deleting"

	echo "Deleting data/network directory"
	rm -rf "data/network"
	echo "Done deleting"

	echo "Deleting data/gpu directory"
	rm -rf "data/gpu"
	echo "Done deleting"

	rm -rf "data/"
}

set_pom_target() {
if [[ $_env == "dev" ]]; then
	echo "Removing prevoius ${pom} file"
	rm $pom
	echo "Using ${dev_pom} file"
	cp $dev_pom pom.xml
else
	echo "Removing prevoius ${pom} file"
	rm $pom
	echo "Using ${prod_pom} file"
	cp $prod_pom pom.xml
fi
}

replace_properties_file() {
	echo "Removing previous ${application_file_properties}"
	rm $application_file_properties
	echo "Using ${application_file_default_properties}"
	cp ${application_file_default_properties} ${application_file_properties}
}

(delete_data_directories)

(set_pom_target)
(replace_properties_file)

mvn clean && mvn package -DskipTests

java -jar "target/${application_name}"
