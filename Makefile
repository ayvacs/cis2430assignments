JC = javac
JVM = java
JCFLAGS = -d $(BIN_DIR) -cp $(SRC_DIR)

SRC_DIR = src
BIN_DIR = bin
DOC_DIR = docs

PACKAGE = dayplanner
MAIN_CLASS = $(PACKAGE).DayPlanner
TEST_CLASS = $(PACKAGE).Tester
JAR = $(BIN_DIR)/DayPlanner.jar

SOURCES := $(shell find $(SRC_DIR) -name "*.java")


# Set default target
all: compile docs jar


# Compile the program
compile:
	$(JC) $(JCFLAGS) $(SOURCES)
	mkdir -p $(BIN_DIR)/$(PACKAGE)
	cp $(SRC_DIR)/$(PACKAGE)/*.properties $(BIN_DIR)/

# Compile and execute the program
run: compile
	$(JVM) -cp $(BIN_DIR) $(MAIN_CLASS)

# Compile and run the automated test suite
test: compile
	$(JVM) -cp $(BIN_DIR) $(TEST_CLASS)

# Build javadocs
docs:
	javadoc -quiet $(SRC_DIR)/**/*.java -d $(DOC_DIR)

# Compile and build executable
jar: compile
	jar cfe $(JAR) $(MAIN_CLASS) -C $(BIN_DIR) .

# Clean output directory
clean:
	rm -rf $(BIN_DIR) $(DOC_DIR)


# Phony rule
.PHONY: all compile run test docs jar clean