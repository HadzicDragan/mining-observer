#!/bin/bash

source env_vars.sh


file_array=("data" "target" "logs", "config")

clear_files() {
	for i in ${!file_array[@]};
	do
	  file_path=${file_array[$i]}
	  echo "Removing ${file_path} files..."
	  rm -rf $file_path
	  echo "Done removing ${file_path} files..."
	  echo " "
	done

    for i in ${!files[@]};
    do
        if [ "$i" -eq "$prod_index" ];then
            echo "Removing previous ${pom} file"
            rm $pom
            echo "Using ${prod_pom} file..."
            cp ${prod_pom} ${pom}
        fi
        file=${files[$i]}
        echo "Removing: ${file} ..."
        rm $file
    done

    ## removing below functionality until it is needed again
    ##
    ## echo "Removing ${test_data}"
    ## rm ${test_data}
}

set_default_properties_file() {
	echo "Removing previous ${application_file_properties} ..."
	rm -rf $application_file_properties
	echo " "
	echo "Applying default application.properties file..."
	cp ${application_file_original_properties} ${application_file_properties}
	echo "File used: ${application_file_original_properties}"
}

set_default_runapp_script() {
    echo "Removing previous run script"
    rm runApp.sh
    echo "Switching to default run script"
    cp "${properties_dir}/${run_script_orig}" .
    mv ${run_script_orig} ${dev_run_script}
}

notify_backup_on_system() {

    echo " "
    echo " "
    echo "#######################################################################################"
    echo "#######################################################################################"
    echo "########################### RUN BACKUP SCRIPT TO LOCAL ################################"
    echo "#######################################################################################"
    echo "#######################################################################################"
    echo " "
    echo " "
}


(clear_files)
(set_default_properties_file)
#(set_default_runapp_script)
(notify_backup_on_system)
