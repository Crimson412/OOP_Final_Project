mkdir -p lib/ out/
rm -rf out/* lib/lib.jar
javac src/oop/model/* src/oop/service/* src/oop/dao/*.java -d out/
jar cf ./lib/lib.jar -C out .
javac -cp "lib/*:." src/oop/main/* -d out/
java -cp "lib/*:out" oop.main.Main
