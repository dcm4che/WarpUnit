package org.dcm4che.warpunit.examples;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PoliteGreeter implements Greeter {
    @Override
    public String greet(String whomToGreet) {
        return "Greetings, " + whomToGreet + " !";
    }
}
