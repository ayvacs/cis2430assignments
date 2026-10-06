# DayPlanner

by Dom Pecak (1376696)

Complete commit history is available on [GitHub](https://github.com/ayvacs/cis2430assignments).

## Directory structure

| Name | Description |
| --- | --- |
| `src` | Source code |
| `bin` | Compiler output |
| `docs` | Javadoc documentation |
| `data` | User-generated data folder

## Instructions

Run the following terminals in the project's root directory (i.e. the same directory where this `README.md` file is located)

### Executing

There are two operations for execution.

1. The first option is to compile and generate a `.jar` executable.
2. The second option is to compile and execute via the JVM.

### Commands

* **Build everything:**
    * compiles the program, generates executable, and generates documentation
    ```
    make
    ```
* **Execute via JVM:**
    ```
    make run
    ```
* **Compile:**
    ```
    make compile
    ```
* **Generate executable:**
    ```
    make jar
    ```
* **Generate documentation:**
    ```
    make docs
    ```
* **Remove generated files:**
    removes all generated folders and subfolders
    ```
    make clean
    ```

# Specifications

## RAS

**Readable Activity Serial** is a format that allows Activities and ActivityLists to be serialized to text files, then read back later.

RAS lists follow the following format:

```
<entry...>
<entry...>
<entry...>
```

where: `<entry...>` is one single RAS entry (which represents one activity) that comprises of the following ordered entries separated by commas:
* A <code>String</code> denoting the activity type.
* A <code>String</code> denoting the activity title.
* <code>String</code> representations of the start and end times.
* A <code>String</code> denoting the comment, or <code>NIL</code> if there is no comment.

RAS lists are saved to `*.ras` files which is simply a standard text file that can be opened in any text editor, however do not edit the contents of these files because it may cause issues in reading and/or writing to them (and this is not tested against because I don't believe it's in the scope of this assignment).