#!/bin/bash

application_name=Observer.jar

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

(delete_data_directories)

mvn clean && mvn package -DskipTests

java -jar "target/${application_name}"
