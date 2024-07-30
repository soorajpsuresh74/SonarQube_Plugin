package com.reflectionsinfos.repository.checks;

import junit.framework.TestCase;
import org.junit.Test;
import org.sonar.python.checks.utils.PythonCheckVerifier;

public class SSNCheckTest {
    @Test
    public void test(){
        PythonCheckVerifier.verify("src/test/resources/SSNCheckTestCheck.py",new SSNCheck());
    }

}