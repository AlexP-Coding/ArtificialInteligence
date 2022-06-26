#!/bin/bash
# to run:
# ./testing.sh testnr
# ex:
# ./testing.sh 01


for inputfile in testes-takuzu/input_T$1
do
    myoutputfile=testes-takuzu/myout_T$1
    outputfile=testes-takuzu/output_T$1
    python3 takuzu.py < $inputfile > $myoutputfile
    diff $myoutputfile $outputfile
done

