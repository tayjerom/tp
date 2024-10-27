# User Guide

## Introduction

Inventra is a command-line inventory management system that helps users to manage their inventory efficiently.

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have Java 17 or above installed.

## Features 

### Deleting a record : `delete`
Deletes a record from the inventory.

Format: 
- `delete <index>`
- `delete -a`
- `delete -e`
- `delete -h <field_name>`
- `delete -r <start>-<end>`

* `delete <index>` deletes the record at the specified index (1-based indexing).
* `delete -a` deletes all records in the inventory.
* `delete -e` deletes all records and headers in the inventory.
* `delete -h <field_name>` deletes the mentioned field and its column from the inventory.
* `delete -r <start>-<end>` deletes records from the start index to the end index (both inclusive and 1-based indexing).

Example of usage: 

- `delete 1`
- `delete -a`
- `delete -e`
- `delete -h price`
- `delete -r 1-5`

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: {your answer here}

## Command Summary

{Give a 'cheat sheet' of commands here}

* Add todo `todo n/TODO_NAME d/DEADLINE`
