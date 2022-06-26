#!/bin/bash
# to run:
# ./testing-stats.sh testnr
# ex:
# ./testing-stats.sh 01


for inputfile in testes-takuzu/input_T$1
do
    myoutputfile=testes-takuzu/myout_T$1
    outputfile=testes-takuzu/output_T$1
		echo 'succs      / goal_tests / states  / found'
		echo 'expandidos / testados   / gerados / estado objetivo achado'
    time python3 takuzu-stats.py < $inputfile
done