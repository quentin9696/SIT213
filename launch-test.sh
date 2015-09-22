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

cmd_base='java Simulateur '

echo '######### Début du test 1  #########'
echo "$cmd_base"
echo ""
$cmd_base 
echo '######### Fin du test 1 #########'

echo '######### Début du test 2  #########'
echo "$cmd_base -mess 11111111111111111"
echo ""
$cmd_base -mess 11111111111111111         
echo '######### Fin du test 2 #########'

echo '######### Début du test 3  #########'
echo "$cmd_base -mess 15"
echo ""
$cmd_base -mess 15
echo '######### Fin du test 3 #########'

echo '######### Début du test 4  #########'
echo "$cmd_base -seed 10"
echo ""
$cmd_base -seed 10
echo '######### Fin du test 4 #########'

echo '######### Début du test 5  #########'
echo "$cmd_base -seed 10"
echo ""
$cmd_base -seed 10
echo '######### Fin du test 5 #########'

echo '######### Début du test 6  #########'
echo "$cmd_base -seed 10 -mess 10"
echo ""
$cmd_base -seed 10 -mess 10
echo '######### Fin du test 6 #########'
