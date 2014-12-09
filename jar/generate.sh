#!/bin/bash
cd ../bin
cp ../jar/MANIFEST.MF .
jar cfm BattleShip.jar MANIFEST.MF *
mv BattleShip.jar ../jar/
rm MANIFEST.MF
