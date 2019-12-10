#!/bin/bash -ex

mvn compile
mvn -e -Dprism.order=sw exec:java -Dexec.mainClass="cs1302.arcade.ArcadeDriver"
