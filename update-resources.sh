#!/bin/sh

filename="temp_resources_archive.tar.gz"
url="https://vk.com/doc63510423_466627577"
out="./src/main/resources/"

mkdir -p ${out}

echo "Downloading from \"${url}\""
wget ${url} --output-document=${filename} --quiet

echo "Extracting ${filename} into \"${out}\""
tar -xzf ${filename} --directory=${out}

echo "Cleaning up"
rm ${filename}
