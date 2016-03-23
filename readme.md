WarpUnit
--------

WarpUnit is a lightweight framework for graybox integration testing of server side java applications.

It allows to use simple junit to implement tests that are partially executed on an application server.

1. You start with some plain junit code.
2. You write some chunks of code as it would be deployed on the server (inside your *.war or *.ear). You can use certain java EE features (e.g. @Inject your CDI beans)
3. When the test is executed, the server code is warp'd to and executed inside your deployment. In the 'client' code you can pass values/see return values and catch exceptions from the server side.

This way you can test your application *as is* i.e. with no need for mocking, and at the same time test against low-level interfaces that are not normally exposed.

To be able to execute warpunit tests against a server, you need to include a special *.war into the deployment (i.e. an *.ear) under test. 