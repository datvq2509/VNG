#!/bin/sh
myArr = $(sudo find $HOME -type f -size +100k)
for ele in $myArr[*]
do
	echo "FILE NAME: "
	echo $ele
	echo "1. DELETE	"
	echo "2. COMPRESS "
	echo "3. SKIP "
	echo "4. EXIT "
	echo "YOUR CHOICE  "
	read choose
	
	case $choose in
	1)
	rm $ele
	time=`date +"[%d/%b/%Y:%k:%M:%S %z]"`
	printf "File name: %s - Time: %s\n" $ele $time >> log.txt
	;;
	2)
	tar -cvzf $ele.tar.gz $ele
	;;
	3)
	continue
	;;
	4)
	break
	;;
	*)
	echo "FAULTS "
	esac
done

