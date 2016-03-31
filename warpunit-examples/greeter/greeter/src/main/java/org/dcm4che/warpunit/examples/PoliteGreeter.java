package org.dcm4che.warpunit.examples;

public class PoliteGreeter implements Greeter {
    @Override
    public String greet(String whomToGreet) {
        return "Greetings, " + whomToGreet + " !";
    }
}
