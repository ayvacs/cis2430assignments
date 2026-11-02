JC = javac
JCFLAGS = -d $(BIN_DIR) -cp $(SRC_DIR)
JVM = java
JVMFLAGS = -cp

SRC_DIR = src
BIN_DIR = bin
DOC_DIR = docs

PACKAGE = dayplanner
MAIN_CLASS = $(PACKAGE).DayPlanner
TEST_CLASS = $(PACKAGE).Tester
JAR = $(BIN_DIR)/DayPlanner.jar
NAME ?= $(PACKAGE)

SRCFILES = $(SRC_DIR)/**/*.java


# Set default target
all: compile docs jar


# Compile the program
compile:
	$(JC) $(JCFLAGS) $(SRCFILES)

# Compile and execute the main program
run: compile
	$(JVM) $(JVMFLAGS) $(BIN_DIR) $(MAIN_CLASS)

# Compile and execute the tester
test: compile
	$(JVM) $(JVMFLAGS) $(BIN_DIR) $(TEST_CLASS)

# Build javadocs
docs:
	javadoc -quiet $(SRCFILES) -d $(DOC_DIR)

# Compile and build executable
jar: compile
	jar cfe $(JAR) $(MAIN_CLASS) -C $(BIN_DIR) .

# Clean output directory
clean:
	rm -rf $(BIN_DIR) $(DOC_DIR)

# Tar and gzip a submission file
zip: clean all
	mkdir -p $(NAME)
	cp -R dat docs src Makefile README.md $(NAME)/
	COPYFILE_DISABLE=1 tar --exclude-from=tar_exclude.txt -cvf $(NAME).tar $(NAME)
	gzip -f $(NAME).tar
	rm -rf $(NAME) $(NAME).tar


# Phony rule
.PHONY: all compile run test docs jar clean zip
