/**
 * Copyright [2020-2022] INESC TEC and VIZLORE Labs Foundation
 * InterConnect Generic Adapter
 * This software is authored by:
 * Aleksandar Tomcic (VIZLORE Labs Foundation)
 * Fábio André Castanheira Luís Coelho (INESC TEC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
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
