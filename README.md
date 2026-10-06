# DayPlanner

by Dom Pecak (1376696)

Complete commit history is available on [GitHub](https://github.com/ayvacs/cis2430assignments).

## Directory structure

| Name | Description |
| --- | --- |
| `src` | Source code |
| `bin` | Compiler output |
| `docs` | Javadoc documentation |

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