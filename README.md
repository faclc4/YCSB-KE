COMPILE AND RUN THE BENCHMARK:

	COMPILE:
	mnv clean compile package install

    RUN THE BENCHMARK:
    ./bin/ycsb.sh com.yahoo.ycsb.Client -p columnfamily=data -t -db com.yahoo.ycsb.KE_Test -P workloads/workloada -s -threads 1
 

