package com.yahoo.ycsb;

import com.yahoo.ycsb.schemes.*;

import java.util.*;

public class KE_Test_96 extends KE{

    private String KE_HOST="http://192.168.112.96:8280/rest";
    private static KEClient keClient;

    public static final int OK=0;
    public static final int ERROR=-1;
    public static final int leaseRenewalTime = 0;
    public static final boolean reasonerEnabled = false;

    @Override
    public void init() throws KEException{
        this.keClient = new KEClient(KE_HOST);
    }

    @Override
    public int createKnowledgeBase(String table, String key, Set<String> fields, HashMap<String, String> result) {
        System.out.println("I am a create KnowledgeBase Operation");
        //CreateSmartConnector createSmartConnector = new CreateSmartConnector(table+key+randomString(),table+key,"test"+key,leaseRenewalTime,reasonerEnabled);
        CreateSmartConnectorNoLease createSmartConnector = new CreateSmartConnectorNoLease(table+key,table+key,"test"+key,reasonerEnabled);
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
        CommunicativeAct act = new CommunicativeAct();
        List<String> purposes = new ArrayList<String>();
        purposes.add("String");
        act.setRequiredPurposes(purposes);
        act.setSatisfiedPurposes(purposes);
        RegisterKnowledgeInteractionPostReact postReact = new RegisterKnowledgeInteractionPostReact("PostKnowledgeInteraction",table+startkey,act,keClient.getArgumentGraphPattern(), keClient.getArgumentGraphPattern(), keClient.getPrefixes());

        int rep = keClient.registerKnowledgeInteractionPostReact(table+startkey,postReact);
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
    public int createReactKI(String table, String key, HashMap<String, String> values) {
        System.out.println("I am a create React KI Operation");
        CommunicativeAct act = new CommunicativeAct();
        List<String> purposes = new ArrayList<String>();
        purposes.add("String");
        act.setRequiredPurposes(purposes);
        act.setSatisfiedPurposes(purposes);
        RegisterKnowledgeInteractionPostReact postReact = new RegisterKnowledgeInteractionPostReact("ReactKnowledgeInteraction",table+key,act,keClient.getArgumentGraphPattern(), keClient.getArgumentGraphPattern(), keClient.getPrefixes());

        int rep = keClient.registerKnowledgeInteractionPostReact(table+key,postReact);
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
    public int createAskKI(String table, String key, HashMap<String, String> values) {
        System.out.println("I am a create Ask Operation");
        CommunicativeAct act = new CommunicativeAct();
        List<String> purposes = new ArrayList<String>();
        purposes.add("String");
        act.setRequiredPurposes(purposes);
        act.setSatisfiedPurposes(purposes);
        RegisterKnowledgeInteractionAskAnswer askAnswer = new RegisterKnowledgeInteractionAskAnswer("AskKnowledgeInteraction",table+key,act,keClient.getArgumentGraphPattern(),keClient.getArgumentGraphPattern(), keClient.getPrefixes());
        int rep = keClient.registerKnowledgeInteractionAskAnswer(table+key,askAnswer);
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
    public int createAnswerKI(String table, String key, HashMap<String, String> values) {
        System.out.println("I am a create Answer Operation");
        CommunicativeAct act = new CommunicativeAct();
        List<String> purposes = new ArrayList<String>();
        purposes.add("String");
        act.setRequiredPurposes(purposes);
        act.setSatisfiedPurposes(purposes);
        RegisterKnowledgeInteractionAskAnswer askAnswer = new RegisterKnowledgeInteractionAskAnswer("AnswerKnowledgeInteraction",table+key,act,keClient.getArgumentGraphPattern(),keClient.getArgumentGraphPattern(), keClient.getPrefixes());
        int rep = keClient.registerKnowledgeInteractionAskAnswer(table+key,askAnswer);
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
    public int deleteKnowledgeBase(String table, String key) {
        System.out.println("I am a delete KnowledgeBase Operation");
        return 0;
    }


    @Override
    public int postBindingSet(String knowledgeBase, String knowledgeInteraction) {
        System.out.println("I am a handle Post Operation");
        int rep = keClient.post(knowledgeBase,knowledgeInteraction, keClient.getBindingSetJSON());
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
    public int askBindingSet(String knowledgeBase, String knowledgeInteraction) {
        System.out.println("I am a handle Ask Operation");
        int rep = keClient.ask(knowledgeBase,knowledgeInteraction, keClient.getBindingSetJSON());
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
    public int handleReact(String knowledgeBase, String knowledgeInteraction) {
        System.out.println("I am a handle React Operation");
        int rep = keClient.startWaitingForHandleRequest(knowledgeBase);
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
    public int handleAnswer(String knowledgeBase, String knowledgeInteraction) {
        System.out.println("I am a handle Answer Operation");
        int rep = keClient.startWaitingForHandleRequest(knowledgeBase);
        if(rep==200){
            System.out.println("200");
            return OK;
        }
        else{
            System.out.println(rep);
            return ERROR;
        }
    }




    private static String randomString(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
