all: build

full:
	./gradlew clean
	./gradlew runData || true
	./gradlew build
	./gradlew reobfJar

build:
	./gradlew build

data:
	./gradlew runData

jar:
	./gradlew reobfJar

test:
	./gradlew runClient

clean:
	./gradlew clean
