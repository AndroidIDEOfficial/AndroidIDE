#!/bin/bash

set -e

print_usage() {
  echo "This script is used to generate ANTL4 lexer."
  echo "Usage :"
  echo "   -u  Download grammar file from URL (using wget) and generate the lexers and parsers the specified directory"
  echo "   -f  Use the specified grammar file for generating lexer and parser"
  echo "   -n  Specify the language code. This language code will be appended to [com.itsaky.androidide.lexers] and used as package name for the generated files."
  echo "   -h  Print this message and exit"
}

dir=$(dirname $0)
dir=$(realpath $dir)
echo $dir

url=''
file=''
name=''

while getopts 'hu:f:n:' flag; do
  case "${flag}" in
    u) url="${OPTARG}";;
    f) file="${OPTARG}";;
    n) name="${OPTARG,,}";;
    *) print_usage
       exit 1 ;;
  esac
done

# Either a file or an URL must be specified
# If not, simply exit
if [ "${url}" == "" ] && [ "${file}" == "" ]
then
  echo "Please specify at least a file or an URL. Use -h for help"
  exit 1
fi

if [ "${name}" == "" ]; then
  echo "Please specify the language code. Use -h for help."
  exit 1
fi

if [ "${url}" != "" ] && [ "${file}" == "" ]; then
  sudo wget -P $dir/src/main/java/com/itsaky/androidide/lexers/$name/ $url
fi

if [ "${url}" != "" ] && [ "${file}" != "" ]; then
  echo "Both URL and file has been specified. Please specify only one"
  exit 1
fi
