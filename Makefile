JC = javac
JVM = java
JCFLAGS = -d $(BIN_DIR) -cp $(SRC_DIR)

SRC_DIR = src
BIN_DIR = bin
DOC_DIR = $(BIN_DIR)/docs

PACKAGE = dayplanner
MAIN_CLASS = $(PACKAGE).DayPlanner

SOURCES := $(shell find $(SRC_DIR) -name "*.java")


# Set default target
all: compile


# Compile the program
compile:
	$(JC) $(JCFLAGS) $(SOURCES)

# Compile and execute the program
run: compile
	$(JVM) -cp $(BIN_DIR) $(MAIN_CLASS)

# Build javadocs
docs:
	javadoc $(SRC_DIR)/**/*.java -d $(DOC_DIR)

# Clean output directory
clean:
	rm -rf $(BIN_DIR)


# Phony rule
.PHONY: all compile run docs clean