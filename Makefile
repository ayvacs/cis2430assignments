# prevent macOS tar from adding unnecessary bloat linux doesn't understand
export COPYFILE_DISABLE=1


JC = javac
JCFLAGS = -d $(BIN_DIR) -cp $(SRC_DIR)
JVM = java
JVMFLAGS = -cp
JDOC = javadoc
JDOCFLAGS = -locale en_CA -quiet -linksource
JJAR = jar
JJARFLAGS = cfe

SRC_DIR = src
BIN_DIR = bin
DOC_DIR = docs

PACKAGE = dayplanner
MAIN_CLASS = $(PACKAGE).DayPlanner
TEST_CLASS = $(PACKAGE).Tester
JAR = $(BIN_DIR)/DayPlanner.jar
NAME ?= $(PACKAGE)

SRCFILES = $(SRC_DIR)/**/*.java


all: compile docs jar


compile:
	$(JC) $(JCFLAGS) $(SRCFILES)

run: compile
	$(JVM) $(JVMFLAGS) $(BIN_DIR) $(MAIN_CLASS)

test: compile
	$(JVM) $(JVMFLAGS) $(BIN_DIR) $(TEST_CLASS)

docs:
	$(JDOC) $(JDOCFLAGS) $(SRCFILES) -d $(DOC_DIR)

jar: compile
	$(JJAR) $(JJARFLAGS) $(JAR) $(MAIN_CLASS) -C $(BIN_DIR) .

clean:
	rm -rf $(BIN_DIR) $(DOC_DIR)

zip: clean all
	mkdir -p $(NAME)
	cp -R dat docs src Makefile README.md $(NAME)/
	tar --exclude-from=tar_exclude.txt --no-xattrs -cf $(NAME).tar $(NAME)
	gzip -f $(NAME).tar
	rm -rf $(NAME) $(NAME).tar


.PHONY: all compile run test docs jar clean zip
