mkdir -p lib/
rm -rf out/* lib/*
javac src/oop/model/* src/oop/service/* -d out/
jar cf ./lib/lib.jar -C out .
javac -cp "lib/lib.jar:." src/oop/main/* -d out/
java -cp "lib/lib.jar:out" oop.main.Main
