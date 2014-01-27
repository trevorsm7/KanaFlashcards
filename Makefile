JFLAGS = -g
JARFLAGS = cvfe
RESOURCES = files/*.txt sound/*.wav
JARNAME = Flashcards.jar
MAINCLASS = Game
CLASSES = Game.java Menu.java MenuItem.java Deck.java Card.java
JC = javac
JAR = jar
.SUFFIXES: .java .class .jar
.java.class:
	$(JC) $(JFLAGS) $*.java

all: classes

classes: $(CLASSES:.java=.class)

jar: classes
	$(JAR) $(JARFLAGS) $(JARNAME) $(MAINCLASS) *.class $(RESOURCES)