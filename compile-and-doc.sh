#!/bin/bash

sources='src'

if [ $# -eq 1 ]
 then	sources=$1
fi

if [ ! -d $sources ]
 then 	echo "Le dossier $sources n'existe pas ... "
	echo "Usage : filename $0 source-java"
	echo "	source-java : nom du repertoire contenant les sources. Si rien n'est mis, le dossier de source est le dossier 'src' du repertoire courrant"
	exit
fi

echo "En continuant, vous aller supprimer le contenu des dossiers 'bin' et 'doc' du repertoire courrant : [y/n]"
read confirm

if [ "$confirm" != "y" -a "$confirm" != "Y" ]
 then	echo "Abandon ..."
	exit
fi

if [ ! -d bin ]
 then 	mkdir bin
fi

if [ ! -d doc ]
 then 	mkdir doc
fi 



rm -rf bin/*
rm -rf doc/*

echo '######### Début de la génération de la javadoc #########'
javadoc -sourcepath $sources -d ./doc -subpackages .
echo '######### Fin de la javadoc #########'

echo '######### Début de la compilation #########'
javac -sourcepath $sources -d ./bin $sources/Simulateur.java
echo '######### Fin de la compilation #########'
