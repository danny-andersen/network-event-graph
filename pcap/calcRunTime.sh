min=$(date +%-M)
let run=59-min
let run=run*60
sec=$(date +%-S)
let run=run+60-sec
echo ${run}
