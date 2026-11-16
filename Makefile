# prevent macOS tar from adding unnecessary bloat linux doesn't understand
export COPYFILE_DISABLE=1


JC = javac
JCFLAGS = -d $(BIN_DIR) -cp $(SRC_DIR)
JVM = java
JVMFLAGS = -cp $(BIN_DIR)
JDOC = javadoc
JDOCFLAGS = -locale en_CA -quiet -linksource
JAR = jar
JARFLAGS = cfe

SRC_DIR = src
BIN_DIR = bin
DOC_DIR = docs

PACKAGE = dayplanner
MAIN_CLASS = $(PACKAGE).DayPlanner
TEST_CLASS = $(PACKAGE).Tester
JARNAME = $(BIN_DIR)/DayPlanner.jar
NAME ?= $(PACKAGE) # .tar.gz name

SRCFILES = $(SRC_DIR)/**/*.java


all: compile docs jar


compile:
	$(JC) $(JCFLAGS) $(SRCFILES)

test: compile
	$(JVM) $(JVMFLAGS) $(TEST_CLASS)

docs:
	$(JDOC) $(JDOCFLAGS) $(SRCFILES) -d $(DOC_DIR)

jar: compile
	$(JAR) $(JARFLAGS) $(JARNAME) $(MAIN_CLASS) -C $(BIN_DIR) .

clean:
	rm -rf $(BIN_DIR) $(DOC_DIR)

zip: clean all
	mkdir -p $(NAME)
	cp -R dat docs src Makefile README.md $(NAME)/
	tar --exclude-from=.tarignore --no-xattrs -cf $(NAME).tar $(NAME)
	gzip -f $(NAME).tar
	rm -rf $(NAME) $(NAME).tar


.PHONY: all compile test docs jar clean zip
