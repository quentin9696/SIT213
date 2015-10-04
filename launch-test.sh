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
echo "$cmd_base -mess 20 -seed 10"
echo ""
$cmd_base -mess 20 -seed 10
echo '######### Fin du test 1 #########'

echo '######### Début du test 2  #########'
echo "$cmd_base -mess 20 -seed 10 -form NRZ"
echo ""
$cmd_base -mess 20 -seed 10 -form NRZ         
echo '######### Fin du test 2 #########'

echo '######### Début du test 3  #########'
echo "$cmd_base -mess 20 -seed -form NRZT"
echo ""
$cmd_base -mess 20 -seed 10 -form NRZT
echo '######### Fin du test 3 #########'

echo '######### Début du test 4  #########'
echo "$cmd_base -form NRZT -ampl -2 2"
echo ""
$cmd_base -form NRZT -ampl -2 2
echo '######### Fin du test 4 #########'

echo '######### Début du test 5  #########'
echo "$cmd_base -form NRZT -ampl -2 1"
echo ""
$cmd_base -form NRZT -ampl -2 1
echo '######### Fin du test 5 #########'

echo '######### Début du test 6  #########'
echo "$cmd_base -form NRZT -ampl -2 5"
echo ""
$cmd_base -form NRZT -ampl -2 5
echo '######### Fin du test 6 #########'

echo '######### Début du test 7  #########'
echo "$cmd_base -form NRZT -ampl 1 5"
echo ""
$cmd_base -form NRZT -ampl 1 5
echo '######### Fin du test 7 #########'

echo '######### Début du test 8  #########'
echo "$cmd_base -form NRZT -mess 40 -nbEch 150"
echo ""
$cmd_base -form NRZT -mess 40 -nbEch 150
echo '######### Fin du test 8 #########'
