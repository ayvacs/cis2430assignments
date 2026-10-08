# DayPlanner

by Dom Pecak (1376696)

Complete commit history is available on [GitHub](https://github.com/ayvacs/cis2430assignments).

# File structure

| Directory | Description |
| --- | --- |
| `bin` | Compiler output |
| `data` | User-generated data folder
| `docs` | Javadoc documentation |
| `src` | Source code |

# User guide

To achieve the desired result (right), run the command on the left. Run all commands in the project's root directory (i.e. the same directory where this `README.md` file is located).


```shell
make            # Build everything

make compile    # Compile program
make run        # Compile program and run via JVM
make jar        # Compile program and generate .jar executable
make docs       # Generate javadoc documentation

make clean      # Remove all generated folders
```

# Implementation

The purpose of this program is to provide an easy way for the user to manage their schedule by permitting them to add and search for Activities and store them in ActivityLists. There are three kinds of activities (Home, School, Other) and data persists between sessions.

My solution assumes:

* The user is comfortable with a command line and executing Java code via the terminal
* There will only ever be two distinguished types of activities the user wishes to enter, with Other being an appropriate substitute for any other type of activity.
* The user does not want a visual representation of their schedule (because my program does not allow for that).

My solution has several limitations:

* The use of Arrays does not allow for the data to be conveniently sorted, and also implements a strict limit on the number of activities.
* The user might not be comfortable with a command line and might prefer a standalone desktop app for the purpose of conveniences.
* There might in the future be more types of activities that the user wants to distinguish between (i.e. Work activities).
* The nature of a command line does not allow for advanced visualization of the data (i.e. as a schedule).

If I had extra time available, I would implement the following improvements:

* Implement ArrayLists to remove the limit on ActivityLists and allow them to be sorted.

## Serialization

RAS (**Readable Activity Serial**) is a custom format I made that allows Activities and ActivityLists to be serialized to text files, then read back later. RAS lists follow the following format:

```
<entry...>
<entry...>
<entry...>
```

where: `<entry...>` is one single RAS entry (which represents one activity) that comprises of the following ordered entries separated by commas:
* A <code>String</code> denoting the type of the activity.
* A <code>String</code> denoting the activity's title.
* <code>String</code> representations of the start and end times.
* A <code>String</code> denoting the comment, or <code>NIL</code> if there is no comment.
* A <code>String</code> denoting the location (applicable only to <code>OtherActivity</code>).

RAS lists are saved to `.ras` files in text format. Do not edit the contents of these files because doing so might cause issues in reading/writing them. <i>(This is not tested against, because I do not believe it is in the scope of this assignment.)</i>

The directory and names of each of the lists can be easily altered in the `src/dayplanner/ras.properties` file. This was implemented to separate the logic code from the user data as much as possible.

I created a custom format rather than using standard serialization so that the contents can be easily inspected, helping during the testing portion of the assignment. (Standard serialization uses binary files rather than text files.)

# Test plan

yada yada I don't wanna do this