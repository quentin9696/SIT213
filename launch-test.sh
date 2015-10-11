#!/bin/bash

sources='bin'

if [ $# -eq 1 ]
 then	sources=$1
fi

if [ ! -d $sources ]
 then 	echo "Le dossier $sources n'existe pas ... "
	echo "Usage : filename $0 binaire-java"
	echo "	binaire-java : nom du repertoire contenant les executable java. Si rien n'est mis, le dossier de binaire est le dossier 'bin' du repertoire courrant"
	exit
fi

cd $sources

cmd_base='java Simulateur -s '

echo '######### Début du test 1  #########'
echo "$cmd_base -mess 20 -seed 10 -snr 5"
echo ""
$cmd_base -mess 20 -seed 10 -snr 5
echo '######### Fin du test 1 #########'

echo '######### Début du test 2  #########'
echo "$cmd_base -mess 20 -seed 10 -form NRZ -snr 5"
echo ""
$cmd_base -mess 20 -seed 10 -form NRZ -snr 5 
echo '######### Fin du test 2 #########'

echo '######### Début du test 3  #########'
echo "$cmd_base -mess 20 -seed -form NRZT -snr 5"
echo ""
$cmd_base -mess 20 -seed 10 -form NRZT -snr 5
echo '######### Fin du test 3 #########'

echo '######### Début du test 4  #########'
echo "$cmd_base -form NRZT -ampl -2 2 -snr 5"
echo ""
$cmd_base -form NRZT -ampl -2 2 -snr 5
echo '######### Fin du test 4 #########'

echo '######### Début du test 5  #########'
echo "$cmd_base -form NRZT -ampl -2 1 -snr 5"
echo ""
$cmd_base -form NRZT -ampl -2 1 -snr 5
echo '######### Fin du test 5 #########'

echo '######### Début du test 6  #########'
echo "$cmd_base -form NRZT -ampl -2 5 -snr 5"
echo ""
$cmd_base -form NRZT -ampl -2 5 -snr 5
echo '######### Fin du test 6 #########'

echo '######### Début du test 7  #########'
echo "$cmd_base -form NRZT -ampl 1 5 -snr 5"
echo ""
$cmd_base -form NRZT -ampl 1 5 -snr 5
echo '######### Fin du test 7 #########'

echo '######### Début du test 8  #########'
echo "$cmd_base -form NRZT -mess 40 -nbEch 150 -snr 5"
echo ""
$cmd_base -form NRZT -mess 40 -nbEch 150 -snr 5
echo '######### Fin du test 8 #########'

echo '######### Début du test 9  #########'
echo "$cmd_base -form NRZT -mess 40 -nbEch 50 -snr -15"
echo ""
$cmd_base -form NRZT -mess 40 -nbEch 50 -snr -15
echo '######### Fin du test 9 #########'

echo '######### Début du test 10  #########'
echo "$cmd_base -nbEch 9 -snr -5"
echo ""
$cmd_base -nbEch 9 -snr -5 
echo '######### Fin du test 10 #########'

echo '######### Début du test 11  #########'
echo "java Histogramme"
echo ""
java Histogramme
echo '######### Fin du test 11 #########'
