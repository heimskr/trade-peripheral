all: jar

jar:
	./gradlew reobfJar

test:
	./gradlew runClient

clean:
	./gradlew clean
