JC = javac
JVM = java
JCFLAGS = -d $(BIN_DIR) -cp $(SRC_DIR)

SRC_DIR = src
BIN_DIR = bin

PACKAGE = dayplanner
MAIN_CLASS = $(PACKAGE).DayPlanner

SOURCES := $(shell find $(SRC_DIR) -name "*.java")


# Set default target
all: compile


# Compilation instructions
compile:
	$(JC) $(JCFLAGS) $(SOURCES)


# Run instructions
run: compile
	$(JVM) -cp $(BIN_DIR) $(MAIN_CLASS)


# Build javadoc instructions
docs: compile
	javadoc src/**/*.java -d bin/docs


# Clean instructions
clean:
	rm -rf $(BIN_DIR)


# Phony
.PHONY: all compile run clean