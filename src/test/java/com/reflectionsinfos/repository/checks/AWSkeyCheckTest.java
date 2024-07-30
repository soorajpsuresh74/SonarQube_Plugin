package com.reflectionsinfos.repository.checks;


import org.junit.Test;
import org.sonar.python.checks.utils.PythonCheckVerifier;

public class AWSkeyCheckTest {
    @Test
    public void test(){
        PythonCheckVerifier.verify("src/test/resources/AWSkeyCheckTestcheck.py", new AWSkeyCheck());
    }
}