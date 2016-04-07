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

rm -rf tmp
mkdir -p tmp/tweeter/library
cp twitter4j-core-4.0.2.jar tmp/tweeter/library/ 
cd bin
jar cvf ../tmp/tweeter/library/tweeter.jar tweeter
cd ..
jar tf tmp/tweeter/library/tweeter.jar
ls -l tmp/tweeter/library/
cp -r reference INSTALL.txt tmp/tweeter/
cd tmp
jar cvf ../Tweeter-$VERSION.zip tweeter 
cd -
echo
ls -l Tweeter-$VERSION.zip



