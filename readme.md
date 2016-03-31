WarpUnit
--------

WarpUnit is a lightweight framework for gray-box integration testing of server side Java EE applications. 

It allows to use simple JUnit to make tests where some parts of the code are executed on an application server directly inside your Java EE application archive. First, this approach allows to avoid extensive mocking, and second, it reduces the risk of missing some deployment aspects while testing.

### Example

Let's say we have a service with the following interface in our application   

``` java  
public interface Greeter {
    String greet(String whomToGreet);
}
```

And we have a CDI bean that implements it  

``` java 
@ApplicationScoped
public class PoliteGreeter implements Greeter {
    @Override
    public String greet(String whomToGreet) {
        return "Greetings, " + whomToGreet + " !";
    }
}
```
Now let's say we want to test this service, and we want to test how it behaves directly on the application server. 
Consider that we don't have this service exposed e.g. through a RESTful service, so we cannot make a "black-box" test for it. 

WarpUnit can help here:
   
``` java 
public class GreeterGrayBoxTest {
 
    @Inject
    Greeter greeter;
 
    @Test
    public void testGreeter() {
 
        System.out.println("This is printed in the JUnit test output");
 
        WarpGate gate = WarpUnit.builder()
                .primaryClass(GreeterGrayBoxTest.class)
                .createGate();
 
        String greetingForBob = gate.warp(() -> {
            System.out.println("This is printed in the server log");
            return greeter.greet("Bob");
        });
 
        Assert.assertEquals("Greetings, Bob !",greetingForBob);
    }
}
``` 

This is a simple JUnit test that you can run within your IDE or with e.g. maven surefire. You need a running server with you application deployed.

So, what happens here?

`WarpGate` is basically a destination where you "warp" your code to run it. You create a gate that is bound to a server (by default it's a server that is running on `localhost`) and you can run code from your test classes upon this gate (i.e. on that server). WarpUnit sends this code over to the server and executes it directly inside you application. You can therefore write some test code just like you would do it if it was part of your application, e.g. you can @Inject your CDI/EJB beans, use EntityManager, etc. In the 'client' code you can pass values/see return values and catch exceptions from the server side.

This way you can test your application *as is* i.e. with no need for mocking, and at the same time test against low-level interfaces/fine-grain services that are not normally exposed.

To be able to execute warpunit tests against a server, you need to include a special `warpunit-insider.war` into your \*.ear under test. You can do it, e.g., with a maven profile to make sure that this war is only included into you application for the purpose of testing. **Make sure it never ends up in production, or you are risking to have a backdoor for arbitrary code execution in you app**.  

You will find this complete example in warpunit-examples/greeter.

#### Current status/limitations

- Tested with Jboss/Wildfly app servers
- Supports java 8
- Lambdas allowed
- Anonymous classes are not allowed. 
- Inner classes (but not inner-of-inner) allowed.
  
#### How it works in a nutshell

Whenever you warp something from the client code, the following happens:
  
On the client  
  
- WarpUnit collects the bytecodes of the class that is warp'd (and it's inner classes)
- Bytecodes are serialized and are sent to the server, along with the info of which method with which parameters should be called

On the server

- The WarpUnit insider deployment receives a RESTful call with all this data 
- The received bytecodes are passed to a classloader and an object of a warp'd class is instantiated on the server 
- CDI injection is performed upon this instance, so all the fields with `@Inject`, `@Resource`, etc annotations are initialized
- The specified method is invoked with the provided parameters
- The return value of the method invocation (or an exception if thrown during execution) is sent as a response of a RESTful service

Back on the client

- The response is returned as an invocation result (or, in case of unsuccessful invocation, a `RemoteExecutionException` is thrown )
