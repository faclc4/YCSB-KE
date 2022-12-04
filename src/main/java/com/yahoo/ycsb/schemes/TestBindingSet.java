package com.yahoo.ycsb.schemes;

public class TestBindingSet {

    public String s;
    public String o;

    public TestBindingSet(String s, String o) {
        this.s = s;
        this.o = o;
    }

    public TestBindingSet() {
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }
}
