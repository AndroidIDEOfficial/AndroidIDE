#!/bin/bash

set -eu

# Usage:
# find * -type d -exec ./gen.sh {} \;

cd $1
dir_name=$(basename $(pwd))
echo "Generating lexer and parser for $dir_name..."
antlr4 -o . -lib . -listener -visitor -package com.itsaky.androidide.lexers.$dir_name *.g4
cd - >/dev/null 2>&1
