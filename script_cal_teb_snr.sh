#!/bin/bash

NBMESS=999999
FORME="NRZ"

cmd_base="java -Xmx30G Simulateur -mess $NBMESS -ampl -1 1 -form $FORME -seed 15 -ti 1 60 0.5 "

echo "$cmd_base" >> "$FORME.txt"
echo "snr,teb" >> "$FORME.txt"
for i in {-60..10}
do
	output=`$cmd_base -snr $i |grep TEB |cut -f2 -d :`
	echo "$i,$output" >> "$FORME.txt"
done
