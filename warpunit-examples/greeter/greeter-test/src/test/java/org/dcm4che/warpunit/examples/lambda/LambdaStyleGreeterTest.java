package org.dcm4che.warpunit.examples.lambda;

import org.dcm4che.warpunit.examples.Greeter;
import org.dcm4che.test.remote.WarpGate;
import org.dcm4che.test.remote.WarpUnit;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;

public class LambdaStyleGreeterTest {

    @Inject
    Greeter greeter;

    @Test
    @Ignore("The server should be running for this test to work")
    public void testGreeter() {

        System.out.println("This is printed in the JUnit test log");

        WarpGate gate = WarpUnit.builder()
                .primaryClass(LambdaStyleGreeterTest.class)
                .createGate();

        String greetingForBob = gate.warp(() -> {
            System.out.println("This is printed in the server log");
            return greeter.greet("Bob");
        });

        Assert.assertEquals("Greetings, Bob !",greetingForBob);
    }
}
