#!/bin/bash

set -eu

source scripts/config.sh

rm -rf wildfly
mkdir wildfly
pushd wildfly

WILDFLY_ARCHIVE="wildfly-$WILDFLY_VERSION.zip"
DOWNLOAD_URL="https://github.com/wildfly/wildfly/releases/download/$WILDFLY_VERSION/$WILDFLY_ARCHIVE"

wget "$DOWNLOAD_URL"
unzip "$WILDFLY_ARCHIVE"
