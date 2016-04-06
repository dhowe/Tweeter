#!/bin/sh
set -e # die on errors 

if [ $# -lt "1"  ]
then
    echo
    echo "  error:   tag or version required"
    echo
    echo "  usage:   make-lib.sh [tag]"
    exit
fi

VERSION=$1

./make-lib.sh $VERSION

RED=rednoise.org
ZIP_DIR=~/www/Tweeter/
ZIP_FILE=Tweeter-$VERSION.zip

cat $ZIP_FILE | /usr/bin/ssh ${USER}@${RED} "(cd ${ZIP_DIR} && /bin/rm -f $ZIP_FILE && cat - > $ZIP_FILE && ln -fs $ZIP_FILE Tweeter-latest.zip; ls -l)"



