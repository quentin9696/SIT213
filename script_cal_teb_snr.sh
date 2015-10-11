#!/bin/bash

NBMESS=20
FORME="NRZ"

cmd_base="java -Xmx8G Simulateur -mess $NBMESS -ampl -1 1 -form $FORME "

echo "$cmd_base" >> "$FORME.txt"

for i in {-60..10}
do
	output=`$cmd_base -snr $i |grep TEB |cut -f2 -d :`
	echo "$i,$output" >> "$FORME.txt"
done
