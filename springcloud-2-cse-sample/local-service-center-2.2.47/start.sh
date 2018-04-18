#!/bin/sh

set -x
set -e

CURRENT_DIR=$(cd $(dirname $0); pwd)

subfix=""

if [ "$(uname)" == "Darwin" ]; then
    # Do something under Mac OS X platform
    subfix="_darwin"
#elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    # Do something under GNU/Linux platform
#elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW32_NT" ]; then
    # Do something under 32 bits Windows NT platform
#elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW64_NT" ]; then
    # Do something under 64 bits Windows NT platform
fi

cd ${CURRENT_DIR}
${CURRENT_DIR}/bin/servicecenter${subfix}
