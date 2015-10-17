#!/bin/bash

NBMESS=1000000
FORME="NRZ"

cmd_base="java  Simulateur  -mess  $NBMESS -nbEch  50 -snr  -4  -seed  10  -ampl  -1  1  -form  $FORME"
#cmd_base="java -Xmx8G Simulateur -mess $NBMESS -ampl -1 1 -form $FORME "

echo "$cmd_base" >> "$FORME.txt"
echo "tau,teb" >> "$FORME.txt"

for i in {25..175}
do
	output=`$cmd_base -ti  1  $i  0.2 |grep TEB |cut -f2 -d :`
	echo "$i,$output" >> "$FORME.txt"
done
