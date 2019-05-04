# Makefile

LogicSimulator:
	javac @classes

tests: tests LogicSimulator
	sh tests

javadoc:
	javadoc @classes

clean:
	rm -f *.class
	rm -f *.html
	rm -f package-list
	rm -f script.js
	rm -f stylesheet.css
