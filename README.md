COMPILAR E CORRER YCSB --> MIDDLEWARE

	COMPILAR:
	javac -cp .:build/ycsb.jar:dist/Middleware.jar:dist/lib/* TransactCassandra.java
	
	CARREGAR DADOS PARA O MIDDLEWARE:
	./bin/ycsb.sh com.yahoo.ycsb.Client -p columnfamily=data -load -db CassandraBulk -P workloads/workloada -s -threadser 1
	
	EFETUAR TESTE WORLOAD NO MIDDLEWARE:
	./bin/ycsb.sh com.yahoo.ycsb.Client -p columnfamily=data -t -db TransactCassandra -P workloads/workloada -s -threads 1

    The Transact and CassandraBluk classes are included within the code.
    They can also be used externally but the package in those classes needs to be removed. 

