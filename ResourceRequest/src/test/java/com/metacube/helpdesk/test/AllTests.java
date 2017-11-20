package com.metacube.helpdesk.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SignUpServicesTest.class, LoginTest.class,
        AdminServiceTest.class, ResourceServiceTest.class,
        ManagerServicesTest.class, TicketServiceTest.class })
public class AllTests {
}