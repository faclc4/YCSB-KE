package com.yahoo.ycsb;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class KE_Test extends KE{
    @Override
    public int createKnowledgeBase(String table, String key, Set<String> fields, HashMap<String, String> result) {
        System.out.println("I am a create KnowledgeBase Operation");
        return 0;
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
