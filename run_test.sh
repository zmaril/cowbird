#!/bin/bash

export PATH=/home/zmaril/software/cowbird/bin:/home/zmaril/bin:/usr/local/bin:/usr/bin:/bin:/usr/local/games:/usr/games:/home/zmaril/bin
export LD_LIBRARY_PATH=/home/zmaril/software/cowbird/resources/postgres/tmp_install/usr/local/pgsql/lib
export PGSSLMODE=disable

cd resources/postgres/src/test/regress
./pg_regress --inputdir=. --temp-instance=./tmp_check --bindir=  --dlpath=. --schedule=./cowbird_schedule --host=localhost --use-existing
