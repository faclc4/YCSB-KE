/**
 * Copyright (c) 2010 Yahoo! Inc. All rights reserved. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying
 * LICENSE file.
 */

package com.yahoo.ycsb.workloads;

import java.util.Properties;
import com.yahoo.ycsb.*;
import com.yahoo.ycsb.generator.CounterGenerator;
import com.yahoo.ycsb.generator.DiscreteGenerator;
import com.yahoo.ycsb.generator.Generator;
import com.yahoo.ycsb.generator.HotspotIntegerGenerator;
import com.yahoo.ycsb.generator.IntegerGenerator;
import com.yahoo.ycsb.generator.ScrambledZipfianGenerator;
import com.yahoo.ycsb.generator.SkewedLatestGenerator;
import com.yahoo.ycsb.generator.UniformIntegerGenerator;
import com.yahoo.ycsb.generator.ZipfianGenerator;
import com.yahoo.ycsb.measurements.Measurements;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;

/**
 * The core benchmark scenario. Represents a set of clients doing simple CRUD operations. The relative 
 * proportion of different kinds of operations, and other properties of the workload, are controlled
 * by parameters specified at runtime.
 * 
 * Properties to control the client:
 * <UL>
 * <LI><b>fieldcount</b>: the number of fields in a record (default: 10)
 * <LI><b>fieldlength</b>: the size of each field (default: 100)
 * <LI><b>readallfields</b>: should reads read all fields (true) or just one (false) (default: true)
 * <LI><b>writeallfields</b>: should updates and read/modify/writes update all fields (true) or just one (false) (default: false)
 * <LI><b>readproportion</b>: what proportion of operations should be reads (default: 0.95)
 * <LI><b>updateproportion</b>: what proportion of operations should be updates (default: 0.05)
 * <LI><b>insertproportion</b>: what proportion of operations should be inserts (default: 0)
 * <LI><b>scanproportion</b>: what proportion of operations should be scans (default: 0)
 * <LI><b>readmodifywriteproportion</b>: what proportion of operations should be read a record, modify it, write it back (default: 0)
 * <LI><b>requestdistribution</b>: what distribution should be used to select the records to operate on - uniform, zipfian, hotspot, or latest (default: uniform)
 * <LI><b>maxscanlength</b>: for scans, what is the maximum number of records to scan (default: 1000)
 * <LI><b>scanlengthdistribution</b>: for scans, what distribution should be used to choose the number of records to scan, for each scan, between 1 and maxscanlength (default: uniform)
 * <LI><b>insertorder</b>: should records be inserted in order by key ("ordered"), or in hashed order ("hashed") (default: hashed)
 * </ul> 
 */
public class CoreWorkload extends Workload
{

	/**
	 * The name of the database table to run queries against.
	 */
	public static final String TABLENAME_PROPERTY="table";

	/**
	 * The default name of the database table to run queries against.
	 */
	public static final String TABLENAME_PROPERTY_DEFAULT="usertable";

	public static String table;


	/**
	 * The name of the property for the number of fields in a record.
	 */
	public static final String FIELD_COUNT_PROPERTY="fieldcount";
	
	/**
	 * Default number of fields in a record.
	 */
	public static final String FIELD_COUNT_PROPERTY_DEFAULT="10";

	int fieldcount;

	/**
	 * The name of the property for the length of a field in bytes.
	 */
	public static final String FIELD_LENGTH_PROPERTY="fieldlength";
	
	/**
	 * The default length of a field in bytes.
	 */
	public static final String FIELD_LENGTH_PROPERTY_DEFAULT="100";

	int fieldlength;

	/**
	 * The name of the property for deciding whether to read one field (false) or all fields (true) of a record.
	 */
	public static final String READ_ALL_FIELDS_PROPERTY="readallfields";
	
	/**
	 * The default value for the readallfields property.
	 */
	public static final String READ_ALL_FIELDS_PROPERTY_DEFAULT="true";

	boolean readallfields;

	/**
	 * The name of the property for deciding whether to write one field (false) or all fields (true) of a record.
	 */
	public static final String WRITE_ALL_FIELDS_PROPERTY="writeallfields";
	
	/**
	 * The default value for the writeallfields property.
	 */
	public static final String WRITE_ALL_FIELDS_PROPERTY_DEFAULT="false";

	boolean writeallfields;


	/**
	 * The name of the property for the proportion of transactions that are reads.
	 */
	public static final String CREATEKB_PROPORTION_PROPERTY="createKBproportion";
	
	/**
	 * The default proportion of transactions that are reads.	
	 */
	public static final String CREATEKB_PROPORTION_PROPERTY_DEFAULT="0.95";

	/**
	 * The name of the property for the proportion of transactions that are updates.
	 */
	public static final String CREATE_ANSWER_KI_PROPORTION_PROPERTY="createKIAnswerproportion";
	
	/**
	 * The default proportion of transactions that are updates.
	 */
	public static final String CREATE_ANSWER_KI_PROPORTION_PROPERTY_DEFAULT="0.05";

	/**
	 * The name of the property for the proportion of transactions that are inserts.
	 */
	public static final String CREATE_ASK_KI_PROPORTION_PROPERTY="createKIASKproportion";
	
	/**
	 * The default proportion of transactions that are inserts.
	 */
	public static final String CREATE_ASK_KI_PROPORTION_PROPERTY_DEFAULT="0.05";

	/**
	 * The name of the property for the proportion of transactions that are scans.
	 */
	public static final String CREATE_REACT_KI_PROPORTION_PROPERTY="createKIReactproportion";
	
	/**
	 * The default proportion of transactions that are scans.
	 */
	public static final String CREATE_REACT_KI_PROPORTION_PROPERTY_DEFAULT="0.05";
	
	/**
	 * The name of the property for the proportion of transactions that are read-modify-write.
	 */
	public static final String CREATE_POST_KI_PROPORTION_PROPERTY="createKIPostproportion";
	
	/**
	 * The default proportion of transactions that are scans.
	 */
	public static final String CREATE_POST_KI_PROPORTION_PROPERTY_DEFAULT="0.05";

	/**
	 * The name of the property for the proportion of transactions that are scans.
	 */
    public static final String MULTI_UPDATE_PROPORTION_PROPERTY="multiupdateproportion";

	/**
	 * The name of the property for the the distribution of requests across the keyspace. Options are "uniform", "zipfian" and "latest"
	 */
	public static final String REQUEST_DISTRIBUTION_PROPERTY="requestdistribution";
	
	/**
	 * The default distribution of requests across the keyspace
	 */
	public static final String REQUEST_DISTRIBUTION_PROPERTY_DEFAULT="uniform";

	/**
	 * The name of the property for the max scan length (number of records)
	 */
	public static final String MAX_SCAN_LENGTH_PROPERTY="maxscanlength";

	/**
	 * The default max scan length.
	 */
	public static final String MAX_SCAN_LENGTH_PROPERTY_DEFAULT="1000";

	/**
	 * The name of the property for the scan length distribution. Options are "uniform" and "zipfian" (favoring short scans)
	 */
	public static final String SCAN_LENGTH_DISTRIBUTION_PROPERTY="scanlengthdistribution";
	
	/**
	 * The default max scan length.
	 */
	public static final String SCAN_LENGTH_DISTRIBUTION_PROPERTY_DEFAULT="uniform";

	public static final String MAX_TRANSACTION_LENGTH_PROPERTY="maxtransactionlength";
	public static final String MAX_TRANSACTION_LENGTH_PROPERTY_DEFAULT="100";
	public static final String TRANSACTION_LENGTH_DISTRIBUTION_PROPERTY="transactionlengthdistribution";
	public static final String TRANSACTION_LENGTH_DISTRIBUTION_PROPERTY_DEFAULT="uniform";

	/**
	 * The name of the property for the order to insert records. Options are "ordered" or "hashed"
	 */
	public static final String INSERT_ORDER_PROPERTY="insertorder";
	
	/**
	 * Default insert order.
	 */
	public static final String INSERT_ORDER_PROPERTY_DEFAULT="hashed";
	
	/**
   * Percentage data items that constitute the hot set.
   */
  public static final String HOTSPOT_DATA_FRACTION = "hotspotdatafraction";
  
  /**
   * Default value of the size of the hot set.
   */
  public static final String HOTSPOT_DATA_FRACTION_DEFAULT = "0.2";
  
  /**
   * Percentage operations that access the hot set.
   */
  public static final String HOTSPOT_OPN_FRACTION = "hotspotopnfraction";
  
  /**
   * Default value of the percentage operations accessing the hot set.
   */
  public static final String HOTSPOT_OPN_FRACTION_DEFAULT = "0.8";
	
	IntegerGenerator keysequence;

	DiscreteGenerator operationchooser;

	IntegerGenerator keychooser;

	Generator fieldchooser;

	CounterGenerator transactioninsertkeysequence;
	
	IntegerGenerator scanlength;
    	IntegerGenerator transactionlength;
	
	boolean orderedinserts;

	int recordcount;
	
	/**
	 * Initialize the scenario. 
	 * Called once, in the main client thread, before any operations are started.
	 */
	public void init(Properties p) throws WorkloadException
	{
		table = p.getProperty(TABLENAME_PROPERTY,TABLENAME_PROPERTY_DEFAULT);
		fieldcount=Integer.parseInt(p.getProperty(FIELD_COUNT_PROPERTY,FIELD_COUNT_PROPERTY_DEFAULT));
		fieldlength=Integer.parseInt(p.getProperty(FIELD_LENGTH_PROPERTY,FIELD_LENGTH_PROPERTY_DEFAULT));


		double createKBproportion=Double.parseDouble(p.getProperty(CREATEKB_PROPORTION_PROPERTY,CREATEKB_PROPORTION_PROPERTY_DEFAULT));

		double createKIAnswerproportion=Double.parseDouble(p.getProperty(CREATE_ANSWER_KI_PROPORTION_PROPERTY,CREATE_ANSWER_KI_PROPORTION_PROPERTY_DEFAULT));
		double createKIAskproportion=Double.parseDouble(p.getProperty(CREATE_ASK_KI_PROPORTION_PROPERTY,CREATE_ASK_KI_PROPORTION_PROPERTY_DEFAULT));

		double createKIPostproportion=Double.parseDouble(p.getProperty(CREATE_POST_KI_PROPORTION_PROPERTY,CREATE_POST_KI_PROPORTION_PROPERTY_DEFAULT));
		double createKIReactproportion=Double.parseDouble(p.getProperty(CREATE_REACT_KI_PROPORTION_PROPERTY,CREATE_REACT_KI_PROPORTION_PROPERTY_DEFAULT));


		recordcount=Integer.parseInt(p.getProperty(Client.RECORD_COUNT_PROPERTY));
		String requestdistrib=p.getProperty(REQUEST_DISTRIBUTION_PROPERTY,REQUEST_DISTRIBUTION_PROPERTY_DEFAULT);
		int maxscanlength=Integer.parseInt(p.getProperty(MAX_SCAN_LENGTH_PROPERTY,MAX_SCAN_LENGTH_PROPERTY_DEFAULT));
		String scanlengthdistrib=p.getProperty(SCAN_LENGTH_DISTRIBUTION_PROPERTY,SCAN_LENGTH_DISTRIBUTION_PROPERTY_DEFAULT);
		
		int maxtransactionlength=Integer.parseInt(p.getProperty(MAX_TRANSACTION_LENGTH_PROPERTY,MAX_TRANSACTION_LENGTH_PROPERTY_DEFAULT));
		String transactionlengthdistrib=p.getProperty(TRANSACTION_LENGTH_DISTRIBUTION_PROPERTY,TRANSACTION_LENGTH_DISTRIBUTION_PROPERTY_DEFAULT);
		
		int insertstart=Integer.parseInt(p.getProperty(INSERT_START_PROPERTY,INSERT_START_PROPERTY_DEFAULT));
		
		readallfields=Boolean.parseBoolean(p.getProperty(READ_ALL_FIELDS_PROPERTY,READ_ALL_FIELDS_PROPERTY_DEFAULT));
		writeallfields=Boolean.parseBoolean(p.getProperty(WRITE_ALL_FIELDS_PROPERTY,WRITE_ALL_FIELDS_PROPERTY_DEFAULT));
		
		if (p.getProperty(INSERT_ORDER_PROPERTY,INSERT_ORDER_PROPERTY_DEFAULT).compareTo("hashed")==0)
		{
			orderedinserts=false;
		}
		else
		{
			orderedinserts=true;
		}

		keysequence=new CounterGenerator(insertstart);
		operationchooser=new DiscreteGenerator();
		if (createKBproportion>0)
		{
			operationchooser.addValue(createKBproportion,"CREATE KNOWLEDGE BASE");
		}

		if (createKIAnswerproportion>0)
		{
			operationchooser.addValue(createKIAnswerproportion,"CREATE ANSWER KI");
		}

		if (createKIAskproportion>0)
		{
			operationchooser.addValue(createKIAskproportion,"CREATE ASK KI");
		}
		
		if (createKIPostproportion>0)
		{
			operationchooser.addValue(createKIPostproportion,"CREATE POST KI");
		}
		
		if (createKIReactproportion>0)
		{
			operationchooser.addValue(createKIReactproportion,"CREATE REACT KI");
		}


		transactioninsertkeysequence=new CounterGenerator(recordcount);
		if (requestdistrib.compareTo("uniform")==0)
		{
			keychooser=new UniformIntegerGenerator(0,recordcount-1);
		}
		else if (requestdistrib.compareTo("zipfian")==0)
		{
			//it does this by generating a random "next key" in part by taking the modulus over the number of keys
			//if the number of keys changes, this would shift the modulus, and we don't want that to change which keys are popular
			//so we'll actually construct the scrambled zipfian generator with a keyspace that is larger than exists at the beginning
			//of the test. that is, we'll predict the number of inserts, and tell the scrambled zipfian generator the number of existing keys
			//plus the number of predicted keys as the total keyspace. then, if the generator picks a key that hasn't been inserted yet, will
			//just ignore it and pick another key. this way, the size of the keyspace doesn't change from the perspective of the scrambled zipfian generator
			
			int opcount=Integer.parseInt(p.getProperty(Client.OPERATION_COUNT_PROPERTY));
			int expectednewkeys=(int)(((double)opcount)*createKBproportion*2.0); //2 is fudge factor
			
			keychooser=new ScrambledZipfianGenerator(recordcount+expectednewkeys);
		}
		else if (requestdistrib.compareTo("latest")==0)
		{
			keychooser=new SkewedLatestGenerator(transactioninsertkeysequence);
		}
		else if (requestdistrib.equals("hotspot")) 
		{
      double hotsetfraction = Double.parseDouble(p.getProperty(
          HOTSPOT_DATA_FRACTION, HOTSPOT_DATA_FRACTION_DEFAULT));
      double hotopnfraction = Double.parseDouble(p.getProperty(
          HOTSPOT_OPN_FRACTION, HOTSPOT_OPN_FRACTION_DEFAULT));
      keychooser = new HotspotIntegerGenerator(0, recordcount - 1, 
          hotsetfraction, hotopnfraction);
    }
		else
		{
			throw new WorkloadException("Unknown distribution \""+requestdistrib+"\"");
		}

		fieldchooser=new UniformIntegerGenerator(0,fieldcount-1);
		
		if (scanlengthdistrib.compareTo("uniform")==0)
		{
			scanlength=new UniformIntegerGenerator(1,maxscanlength);
		}
		else if (scanlengthdistrib.compareTo("zipfian")==0)
		{
			scanlength=new ZipfianGenerator(1,maxscanlength);
		}
		else
		{
			throw new WorkloadException("Distribution \""+scanlengthdistrib+"\" not allowed for scan length");
		}

		
		if (transactionlengthdistrib.compareTo("uniform")==0)
		{
			transactionlength=new UniformIntegerGenerator(1,maxtransactionlength);
		}
		else if (transactionlengthdistrib.compareTo("zipfian")==0)
		{
			transactionlength=new ZipfianGenerator(1,maxtransactionlength);
		}
		else
		{
			throw new WorkloadException("Distribution \""+transactionlengthdistrib+"\" not allowed for transaction length");
		}
	}

	/**
	 * Do one insert operation. Because it will be called concurrently from multiple client threads, this 
	 * function must be thread safe. However, avoid synchronized, or the threads will block waiting for each 
	 * other, and it will be difficult to reach the target throughput. Ideally, this function would have no side
	 * effects other than DB operations.
	 */
	public boolean doCreateAnswerKI(KE db, Object threadstate)
	{
		int keynum=keysequence.nextInt();
		if (!orderedinserts)
		{
			keynum=Utils.hash(keynum);
		}
		String dbkey="user"+keynum;
		HashMap<String,String> values=new HashMap<String,String>();
		for (int i=0; i<fieldcount; i++)
		{
			String fieldkey="field"+i;
			String data=Utils.ASCIIString(fieldlength);
			values.put(fieldkey,data);
		}
		if (db.createAnswerKI(table,dbkey,values) == 0)
			return true;
		else
			return false;
	}

	/**
	 * Do one transaction operation. Because it will be called concurrently from multiple client threads, this 
	 * function must be thread safe. However, avoid synchronized, or the threads will block waiting for each 
	 * other, and it will be difficult to reach the target throughput. Ideally, this function would have no side
	 * effects other than DB operations.
	 */
	public boolean doTransaction(KE db, Object threadstate)
	{
		String op=operationchooser.nextString();

		if (op.compareTo("CREATE ANSWER KI")==0)
		{
			doCreateAnswerKI(db);
		}
		else if (op.compareTo("CREATE ASK KI")==0)
		{
			doCreateAskKI(db);
		}
		else if (op.compareTo("CREATE REACT KI")==0)
		{
			doCreateReactKI(db);
		}
		else if (op.compareTo("CREATE POST KI")==0)
		{
			doCreatePostKI(db);
		}
 		else if (op.compareTo("CREATE KNOWLEDGE BASE")==0)
		{
			doCreateKnowledgeBase(db);
		}

		else
		{
			//doTransactionReadModifyWrite(db);
		}
		
		return true;
	}

	//read
	public void doCreateKnowledgeBase(KE db)
	{
		//choose a random key
		int keynum;
		do
		{
			keynum=keychooser.nextInt();
		}
		while (keynum>transactioninsertkeysequence.lastInt());
		
		if (!orderedinserts)
		{
			keynum=Utils.hash(keynum);
		}
		String keyname="user"+keynum;

		HashSet<String> fields=null;

		if (!readallfields)
		{
			//read a random field  
			String fieldname="field"+fieldchooser.nextString();

			fields=new HashSet<String>();
			fields.add(fieldname);
		}

		long st=System.currentTimeMillis();
		db.createKnowledgeBase(table,keyname,fields,new HashMap<String,String>());
		long en=System.currentTimeMillis();

		Measurements.getMeasurements().measure("CREATE KNOWLEDGE BASE", (int)(en-st));
	}

	//readModifyWrite
	public void doCreateAskKI(KE db)
	{
		//choose a random key
		int keynum;
		do
		{
			keynum=keychooser.nextInt();
		}
		while (keynum>transactioninsertkeysequence.lastInt());
		
		if (!orderedinserts)
		{
			keynum=Utils.hash(keynum);
		}
		String keyname="user"+keynum;

		HashSet<String> fields=null;

		if (!readallfields)
		{
			//read a random field  
			String fieldname="field"+fieldchooser.nextString();

			fields=new HashSet<String>();
			fields.add(fieldname);
		}
		
		HashMap<String,String> values=new HashMap<String,String>();

		if (writeallfields)
		{
		   //new data for all the fields
		   for (int i=0; i<fieldcount; i++)
		   {
		      String fieldname="field"+i;
		      String data=Utils.ASCIIString(fieldlength);		   
		      values.put(fieldname,data);
		   }
		}
		else
		{
		   //update a random field
		   String fieldname="field"+fieldchooser.nextString();
		   String data=Utils.ASCIIString(fieldlength);		   
		   values.put(fieldname,data);
		}

		//do the transaction

		long st=System.currentTimeMillis();
		db.createKnowledgeBase(table,keyname,fields,new HashMap<String,String>());
		long en=System.currentTimeMillis();

		Measurements.getMeasurements().measure("CREATE KNOWLEDGE BASE", (int)(en-st));
		
		st=System.currentTimeMillis();
		db.createAskKI(table,keyname,new HashMap<String,String>());
		en=System.currentTimeMillis();

		Measurements.getMeasurements().measure("CREATE ASK KI", (int)(en-st));
	}

	//scan
	public void doCreatePostKI(KE db)
	{
		//choose a random key
		int keynum;
		do
		{
			keynum=keychooser.nextInt();
		}
		while (keynum>transactioninsertkeysequence.lastInt());

		if (!orderedinserts)
		{
			keynum=Utils.hash(keynum);
		}
		String startkeyname="user"+keynum;
		
		//choose a random scan length
		int len=scanlength.nextInt();

		HashSet<String> fields=null;

		if (!readallfields)
		{
			//read a random field  
			String fieldname="field"+fieldchooser.nextString();

			fields=new HashSet<String>();
			fields.add(fieldname);
		}

		long st=System.currentTimeMillis();
		db.createKnowledgeBase(table,startkeyname,fields,new HashMap<String,String>());
		long en=System.currentTimeMillis();

		Measurements.getMeasurements().measure("CREATE KNOWLEDGE BASE", (int)(en-st));

		st=System.currentTimeMillis();
		db.createPostKI(table,startkeyname,len,fields,new Vector<HashMap<String,String>>());
		en=System.currentTimeMillis();

		Measurements.getMeasurements().measure("CREATE POST KI", (int)(en-st));

	}

	//Update
	public void doCreateReactKI(KE db)
	{
		//choose a random key
		int keynum;
		do
		{
			keynum=keychooser.nextInt();
		}
		while (keynum>transactioninsertkeysequence.lastInt());

		if (!orderedinserts)
		{
			keynum=Utils.hash(keynum);
		}
		String keyname="user"+keynum;

		HashMap<String,String> values=new HashMap<String,String>();

		if (writeallfields)
		{
		   //new data for all the fields
		   for (int i=0; i<fieldcount; i++)
		   {
		      String fieldname="field"+i;
		      String data=Utils.ASCIIString(fieldlength);		   
		      values.put(fieldname,data);
		   }
		}
		else
		{
		   //update a random field
		   String fieldname="field"+fieldchooser.nextString();
		   String data=Utils.ASCIIString(fieldlength);		   
		   values.put(fieldname,data);
		}

		long st=System.currentTimeMillis();
		db.createKnowledgeBase(table,keyname,null,new HashMap<String,String>());
		long en=System.currentTimeMillis();

		Measurements.getMeasurements().measure("CREATE KNOWLEDGE BASE", (int)(en-st));

		st=System.currentTimeMillis();
		db.createReactKI(table,keyname,values);
		en=System.currentTimeMillis();

		Measurements.getMeasurements().measure("CREATE REACT KI", (int)(en-st));
	}

	//updateMulti
	public void doRemoveKnowledgeBase(KE db)
	{
	    //choose a random scan length
	    int len=10; //transactionlength.nextInt();

		List<String> keys = new ArrayList<String>(len);
		for (int i = 0; i < len; i++) {
		    //choose a random key
		    int keynum;
		    do
			{
			    keynum=keychooser.nextInt();
			}
		    while (keynum>transactioninsertkeysequence.lastInt());
		    
		    if (!orderedinserts)
			{
			    keynum=Utils.hash(keynum);
			}
		    keys.add("user"+keynum);
		}		    

		HashMap<String,String> values=new HashMap<String,String>();

		if (writeallfields)
		{
		   //new data for all the fields
		   for (int i=0; i<fieldcount; i++)
		   {
		      String fieldname="field"+i;
		      String data=Utils.ASCIIString(fieldlength);		   
		      values.put(fieldname,data);
		   }
		}
		else
		{
		   //update a random field
		   String fieldname="field"+fieldchooser.nextString();
		   String data=Utils.ASCIIString(fieldlength);		   
		   values.put(fieldname,data);
		}

		//db.deleteKnowledgeBase(table,keys);

	}

	//insert
	public void doCreateAnswerKI(KE db)
	{
		//choose the next key
		int keynum=transactioninsertkeysequence.nextInt();
		if (!orderedinserts)
		{
			keynum=Utils.hash(keynum);
		}
		String dbkey="user"+keynum;
		
		HashMap<String,String> values=new HashMap<String,String>();
		for (int i=0; i<fieldcount; i++)
		{
			String fieldkey="field"+i;
			String data=Utils.ASCIIString(fieldlength);
			values.put(fieldkey,data);
		}

		long st=System.currentTimeMillis();
		db.createKnowledgeBase(table,dbkey,null,new HashMap<String,String>());
		long en=System.currentTimeMillis();

		Measurements.getMeasurements().measure("CREATE KNOWLEDGE BASE", (int)(en-st));

		st=System.currentTimeMillis();
		db.createAnswerKI(table,dbkey,values);
		en=System.currentTimeMillis();

		Measurements.getMeasurements().measure("CREATE ANSWER KI", (int)(en-st));
	}
}
