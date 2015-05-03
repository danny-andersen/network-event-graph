#!/bin/bash
root=$(dirname $0)
runtime=$(${root}/calcRunTime.sh)
nextFile=$(${root}/nextFileName.sh ${root})
${root}/exec_tshark.sh $runtime $nextFile
