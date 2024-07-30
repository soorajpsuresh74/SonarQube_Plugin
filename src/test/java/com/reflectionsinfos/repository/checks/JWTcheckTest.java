package com.reflectionsinfos.repository.checks;

import org.junit.jupiter.api.Test;
import org.sonar.python.checks.utils.PythonCheckVerifier;

public class JWTcheckTest{
    @Test
    public void myTest(){
        PythonCheckVerifier.verify("src/test/resources/JwtcheckTestcheck.py",new JWTcheck());
    }
}