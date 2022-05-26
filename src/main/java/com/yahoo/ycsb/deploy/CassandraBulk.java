package com.yahoo.ycsb.deploy;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.yahoo.ycsb.DB;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;


import pt.haslab.middleware.NoSQL.CassandraAPI;
import pt.haslab.middleware.NoSQL.CassandraClient;
import pt.haslab.middleware.versions.VersionedRowMap;
import pt.haslab.middleware.versions.VersionedValue;

/**
 * This class provides support to bulk load data from YCSB to Cassandra .
 * @author Fabio Coelho
 * MSc Thesis: Implementation and test of transactional primitives over cassandra - 2012/2013
 */
public class CassandraBulk extends DB{
    
    public static final int OK=0;
    public static final int ERROR=-1;
    
    public static CassandraClient db_client;
    public static Keyspace keyspace;
    
    static{
        db_client = CassandraAPI.getClient();
        keyspace = db_client.getKeyspace();
    }

    @Override
    public int read(String table, String key, Set<String> columns, HashMap<String, String> values) {
        try{
            List<String> cols;
            if(columns == null){
                cols = null;
            }
            else{
                cols = new ArrayList<String>(columns);
            }
            Map<String,Object> results = new TreeMap<String, Object>();

            VersionedRowMap db_values = db_client.get(table,key, cols);
            if(db_values!=null){
                for (Map.Entry<String, Object> result_item : db_values.getValueMap().entrySet()) {
                    results.put(result_item.getKey(), result_item.getValue());
                }        
            }
            else{
                results=null;                
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return ERROR;
        }
        return OK;
    }

    @Override
    public int update(String table, String key, HashMap<String, String> values) {
        return insert(table,key,values);        
    }

    @Override
    public int insert(String table, String key, HashMap<String, String> values) {
        try{
            for(Map.Entry<String,String> value : values.entrySet()){
                db_client.set(table, key, value.getKey(), value.getValue(), 0);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return ERROR;
        }
        return OK;
    }

    @Override
    public int delete(String table, String key) {
        try{
            db_client.remove_row(table, key);
        }
        catch(Exception e){
            e.printStackTrace();
            return ERROR;
        }
        return OK;
    }

    @Override
    public int scan(String table, String key, int count, Set<String> columns, Vector<HashMap<String, String>> res) {
        try{
            List<String> cols;
        if(columns == null){
            cols = null;
        }
        else{
            cols = new ArrayList<String>(columns);
        }
        Map<String,VersionedRowMap> results = null;
        
        db_client.scan(table, key, "", cols);
        
        if(results != null){
            //for each key...
            for(Map.Entry<String,VersionedRowMap> result : results.entrySet()){
                HashMap<String,String> row = new HashMap<String,String>();
                
                //for each column
                for(Map.Entry<String,Object> column : result.getValue().getValueMap().entrySet()){
                    row.put(column.getKey(), (String)column.getValue());
                }
                res.add(row);                
            }
        }
        else{
            res = null;
            return ERROR;
        }
        
        
        }
        catch(Exception e){
            e.printStackTrace();
            return ERROR;
        }
        
        return OK;
    }
    
    @Override
    public int updateMulti(String table, List<String> keys, HashMap<String, String> values){
        try{
            for(String key : keys){
                for(Map.Entry<String,String> value : values.entrySet()){
                    db_client.set(table, key, value.getKey(), value.getValue(), 0);
                }
            }
        }
        catch(Exception e){
            System.err.println("could no perform MultiUpdate");
            return ERROR;
        }
        return OK;
    }
}

