package com.yahoo.ycsb;

import com.yahoo.ycsb.schemes.CreateSmartConnector;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class KE_Test extends KE{

    private String KE_HOST="http://localhost:8280/rest";
    private static KEClient keClient;

    public static final int OK=0;
    public static final int ERROR=-1;

    @Override
    public void init() throws KEException{
        this.keClient = new KEClient(KE_HOST);
    }

    @Override
    public int createKnowledgeBase(String table, String key, Set<String> fields, HashMap<String, String> result) {
        System.out.println("I am a create KnowledgeBase Operation");
        CreateSmartConnector createSmartConnector = new CreateSmartConnector(table+key,table+key,"test"+key,30,false);
        int rep = keClient.createSmartConnector(createSmartConnector);
        if(rep==200){
            System.out.println("200");
            return OK;
        }
        else{
            System.out.println(rep);
            return ERROR;
        }
    }

    @Override
    public int createPostKI(String table, String startkey, int recordcount, Set<String> fields, Vector<HashMap<String, String>> result) {
        System.out.println("I am a create PostKI Operation");
        return 0;
    }

    @Override
    public int createReactKI(String table, String key, HashMap<String, String> values) {
        System.out.println("I am a create React KI Operation");
        return 0;
    }

    @Override
    public int createAnswerKI(String table, String key, HashMap<String, String> values) {
        System.out.println("I am a create Answer Operation");
        return 0;
    }

    @Override
    public int deleteKnowledgeBase(String table, String key) {
        System.out.println("I am a delete KnowledgeBase Operation");
        return 0;
    }
}
