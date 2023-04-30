#!/bin/bash

set -eu

dest=$1
url=$2
user=$3
pass=$4

if [ "$dest" == "" ]; then
    echo "Please specify destination file"
    exit 1
fi

if [ "$url" == "" ]; then
    echo "Please specify signing key URL"
    exit 1
fi

if [ "$user" == "" ]; then
    echo "Please specify auth user"
    exit 1
fi

if [ "$pass" == "" ]; then
    echo "Please specify auth password"
    exit 1
fi

if [ -f "$dest" ]; then
    echo "Skipping signing key download as $dest already exists"
    exit 0
fi

# Download silently to prevent URL/user/pass being exposed
curl -Lsu "$user:$pass" --output "$dest" $url --http1.1
