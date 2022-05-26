import com.yahoo.ycsb.DB;
import com.yahoo.ycsb.DBException;
import pt.haslab.middleware.snapshot.Transaction;
import pt.haslab.middleware.transaction_manager.LocalTransactionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides support to test the middleware with Ycsb benchmark.
 * @author Fabio Coelho
 * MSc Thesis: Implementation and test of transactional primitives over cassandra - 2012/2013
 */
public class TransactCassandra extends DB{
    
    public static final int OK=0;
    public static final int ERROR=-1;
    
    public static LocalTransactionManager tm;
    
    static{
        try {
            tm = new LocalTransactionManager();
        } catch (IOException ex) {
            //Logger.getLogger(TransactCassandra.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            //Logger.getLogger(TransactCassandra.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public int read(String table, String key, Set<String> columns, HashMap<String, String> res) {
        Transaction trx = null;
        List<String> cols;
        if(columns == null){
            cols = null;
        }
        else{
            cols = new ArrayList<String>(columns);
        }
        Map<String,Object> results = null;
        
        try {
            trx = tm.beginTransaction();
            
        } catch (Exception ex) {
            System.err.println("Error creating new transaction trying to perform read operation");
            return ERROR;
        }
        
        try {
            results = trx.get(table, key, cols);
            
        } catch (Exception ex) {
            System.err.println("couldn't retrieve data performing READ operation");
            return ERROR;
        }
        
        if(results!=null){            
            for(Map.Entry<String,Object> result: results.entrySet()){
                res.put(key, (String)result.getValue());
            }
        }
        else res = null;
        try {
            tm.tryCommit(trx);
        } catch (Exception ex) {
            System.err.println("Couldn't commit performing read transaction");
            return ERROR;
        }
        
        return OK;
    }

    /**
     *
     * @param table
     * @param key
     * @param i
     * @param columns
     * @param res
     * @return
     */
    @Override
    public int scan(String table, String key, int i, Set<String> columns, Vector<HashMap<String, String>> res) {
        Transaction trx = null;
        List<String> cols;
        if(columns == null){
            cols = null;
        }
        else{
            cols = new ArrayList<String>(columns);
        }
        Map<String,Map<String,Object>> results = null;
        
        try {
            trx = tm.beginTransaction();
            
        } catch (Exception ex) {
            System.err.println("Error creating new transaction trying to perform read operation");
            return ERROR;
        }
        try {
            results = trx.scan(table, key, "", cols);            
        } catch (Exception ex) {
            System.err.println("couldn't retrieve data performing SCAN operation");
            return ERROR;
        }
        
        if(results != null){
            //for each key...
            for(Map.Entry<String,Map<String,Object>> result : results.entrySet()){
                HashMap<String,String> row = new HashMap<String,String>();
                
                //for each column
                for(Map.Entry<String,Object> column : result.getValue().entrySet()){
                    row.put(column.getKey(), (String)column.getValue());
                }
                res.add(row);                
            }
        }
        else{
            res = null;
            return ERROR;
        }
        
        try {
            boolean result = tm.tryCommit(trx);
            System.out.println("[Transaction: "+ trx.getReadStamp() +"] --> commit was: "+result);
        } catch (Exception ex) {
            System.err.println("Couldn't commit performing scan transaction");
            return ERROR;
        }
        
        return OK;
    }

    @Override
    public int update(String table, String key, HashMap<String, String> cols) {
        int res = insert(table,key,cols);
        return res;
    }

    @Override
    public int updateMulti(String table, List<String> keys, HashMap<String, String> values){
        Transaction trx = null;
        
        try {
            trx = tm.beginTransaction();
            
        } catch (Exception ex) {
            System.err.println("Error creating new transaction trying to perform write operation");
            return ERROR;
        }
        
        for(String key : keys){
            for(Map.Entry<String,String> value : values.entrySet()){
                trx.write(table, key, value.getKey(), value.getValue());
            }
        }
        try {
            boolean result = tm.tryCommit(trx);
            System.out.println("[Transaction: "+ trx.getReadStamp() +"] --> commit was: "+result);
        } catch (Exception ex) {
            System.err.println("Couldn't commit performing write transaction");
            return ERROR;
        }
        
        return OK;
    }
    
    /**
     *
     * @param table
     * @param key
     * @param values
     * @return
     */
    @Override
    public int insert(String table, String key, HashMap<String, String> values) {
        Transaction trx = null;
        
        try {
            trx = tm.beginTransaction();
            
        } catch (Exception ex) {
            System.err.println("Error creating new transaction trying to perform write operation");
            return ERROR;
        }
        
        for(Map.Entry<String,String> value : values.entrySet()){
            trx.write(table, key, value.getKey(), value.getValue());
        }
        
        try {
            boolean result = tm.tryCommit(trx);
        } catch (Exception ex) {
            System.err.println("Couldn't commit performing write transaction");
            return ERROR;
        }
        
        return OK;
    }

    @Override
    public int delete(String table, String key) {
        Transaction trx = null;
        
        try {
            trx = tm.beginTransaction();
            
        } catch (Exception ex) {
            System.err.println("Error creating new transaction trying to perform delete operation");
            return ERROR;
        }
        trx.delete(table, key);
        
        try {
            boolean result = tm.tryCommit(trx);
            System.out.println("[Transaction: "+ trx.getReadStamp() +"] --> commit was: "+result);
        } catch (Exception ex) {
            System.err.println("Couldn't commit performing delete transaction");
            return ERROR;
        }
        
        return OK;
    }

    @Override
    public void cleanup(){
        System.out.println("*******STATISTICS FOR THIS TRANSACTION MANAGER *********");
        tm.print_stats();
    }
}
