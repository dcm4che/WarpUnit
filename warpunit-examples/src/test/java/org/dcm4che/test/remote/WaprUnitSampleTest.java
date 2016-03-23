/*
 * *** BEGIN LICENSE BLOCK *****
 *  Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 *  The contents of this file are subject to the Mozilla Public License Version
 *  1.1 (the "License"); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *  http://www.mozilla.org/MPL/
 *
 *  Software distributed under the License is distributed on an "AS IS" basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *  for the specific language governing rights and limitations under the
 *  License.
 *
 *  The Original Code is part of dcm4che, an implementation of DICOM(TM) in
 *  Java(TM), hosted at https://github.com/gunterze/dcm4che.
 *
 *  The Initial Developer of the Original Code is
 *  Agfa Healthcare.
 *  Portions created by the Initial Developer are Copyright (C) 2015
 *  the Initial Developer. All Rights Reserved.
 *
 *  Contributor(s):
 *  See @authors listed below
 *
 *  Alternatively, the contents of this file may be used under the terms of
 *  either the GNU General Public License Version 2 or later (the "GPL"), or
 *  the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 *  in which case the provisions of the GPL or the LGPL are applicable instead
 *  of those above. If you wish to allow use of your version of this file only
 *  under the terms of either the GPL or the LGPL, and not to allow others to
 *  use your version of this file under the terms of the MPL, indicate your
 *  decision by deleting the provisions above and replace them with the notice
 *  and other provisions required by the GPL or the LGPL. If you do not delete
 *  the provisions above, a recipient may use your version of this file under
 *  the terms of any one of the MPL, the GPL or the LGPL.
 *
 *  ***** END LICENSE BLOCK *****
 */

package org.dcm4che.test.remote;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Resource;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

public class WaprUnitSampleTest {

    @Resource(lookup = "java:jboss/TransactionManager")
    private TransactionManager tmManager;

    @Resource(lookup="java:app/AppName")
    private String appName;

    @Test
    @Ignore("To runt this test you need a running server with a warpunit-insider deployed")
    public void helloWorld() throws Exception {

        WarpGate gate = WarpUnit.createGate(WaprUnitSampleTest.class);

        String closure = "Hello from client!";

        // this stuff is ran on the server
        int txStatusOnServer = gate.warp(() -> {

            System.out.println("I'm inside the server! I see the closure from the client: " + closure);

            try {

                System.out.println("I can see stuff here, e.g. deployment name is " + appName);

                // We can return values back, let's try to return the current transaction status
                return tmManager.getStatus();

            } catch (SystemException e) {
                throw new RuntimeException("This will be thrown on a client if thrown here", e);
            }

        });

        // it's a test after all so we need to assert something
        // e.g. there should have been no active transaction when we executed that piece of code
        Assert.assertEquals(Status.STATUS_NO_TRANSACTION, txStatusOnServer);

    }
}
