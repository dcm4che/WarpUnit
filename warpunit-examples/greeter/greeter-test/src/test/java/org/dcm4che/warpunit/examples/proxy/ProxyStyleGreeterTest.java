package org.dcm4che.warpunit.examples.proxy;


import org.dcm4che.test.remote.WarpUnit;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ProxyStyleGreeterTest {

    @Test
    @Ignore("The server should be running for this test to work")
    public void testGreeterWithProxy() {

        GreeterInsider proxyGate = WarpUnit.builder()
                .primaryClass(GreeterInsiderImpl.class)
                .includeInterface(true)
                .createProxyGate(GreeterInsider.class);

        System.out.println("This is printed in the JUnit test log");

        String greetingForBob = proxyGate.getAGreeting("Bob");

        Assert.assertEquals("Greetings, Bob !", greetingForBob);
    }

}
