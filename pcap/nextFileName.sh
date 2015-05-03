root=$1
d=$(date +%Y%m%d)
mkdir -p $root/$d
hr=$(date +%H)
nextFile=${root}/${d}/${hr}.out
part=1
while [ -f $nextFile ]
do
	nextFile=${root}/${d}/${hr}_${part}.out
	let part=part+1
done
echo ${nextFile}
