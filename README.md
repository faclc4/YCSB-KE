This project is derived from Yahoo's YCSB Benchmark.
It was tailored to access the Knowledge Engine component for semantic orchestration and exchange of data.

CHANGE LOG:

com.yahoo.ycsb.workloads.CoreWorkload
com.yahoo.ycsb.DB becomes com.yahoo.ycsb.KE
com.yahoo.ycsb.DBClient becomes com.yahoo.ycsb.KEClient
com.yahoo.ycsb.DBException becomes com.yahoo.ycsb.KEException
com.yahoo.ycsb.DBFactory becomes com.yahoo.ycsb.KEFactory
com.yahoo.ycsb.DBWrapper becomes com.yahoo.ycsb.KEWrapper

workloads/workloada
workloads/workloadb
workloads/workloadc


COMPILE AND RUN THE BENCHMARK:

	COMPILE:
	mnv clean compile package install

    RUN THE BENCHMARK:
    ./bin/ycsb.sh com.yahoo.ycsb.Client -p columnfamily=data -t -db com.yahoo.ycsb.KE_Test_96 -P workloads/workloada -s -threads 1
 

