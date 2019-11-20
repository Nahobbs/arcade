#!/bin/bash -ex

javac -d target/classes/ src/main/java/cs1302/mancala/Mancala.java
java -cp target/classes/ -Dprism.order=sw cs1302.mancala.Mancala
