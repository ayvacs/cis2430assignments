JC = javac
JVM = java
JCFLAGS = -d $(BIN_DIR) -cp $(SRC_DIR)

SRC_DIR = dayplanner
BIN_DIR = bin

PACKAGE = dayplanner
MAIN_CLASS = $(PACKAGE).DayPanner

SOURCES := $(shell find $(SRC_DIR) -name "*.java")

all: compile

compile:
	$(JC) $(JCFLAGS) $(SOURCES)

run: compile
	$(JVM) -cp $(BIN_DIR) $(MAIN_CLASS)