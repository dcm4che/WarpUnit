package org.dcm4che.warpunit.examples.proxy;

import org.dcm4che.test.remote.WarpGate;
import org.dcm4che.test.remote.WarpUnit;
import org.dcm4che.warpunit.examples.Greeter;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.inject.Inject;

public class GreeterInsiderImpl implements GreeterInsider {

    @Inject
    Greeter greeter;

    @Override
    public String getAGreeting(String forWhom) {
        System.out.println("This is printed in the server log");
        return greeter.greet(forWhom);
    }
}
