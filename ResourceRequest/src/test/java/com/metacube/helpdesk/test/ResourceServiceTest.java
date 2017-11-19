package com.metacube.helpdesk.test;

import static org.junit.Assert.assertEquals;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.metacube.helpdesk.service.ResourceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "test-config.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResourceServiceTest {

    @Autowired
    ResourceService resourceService;
    static final String EMPLOYEE_USERNAME = "udit.saxena@metacube.com";
    static final String EMPLOYEE_AUTHORISATION_TOKEN = "c06f126dd6d510afe0bfe4d0191dd38c";

    @Test
    public void test30_getAllResourcesCategoryWhenLoggedInUserIsInvalid() {
        assertEquals(
                0,
                resourceService.getAllResourceCategory("b789003ghjk678999900",
                        EMPLOYEE_USERNAME).size());
    }

    @Test
    public void test30_getAllResourcesCategoryWhenLoggedInUserIsvalid() {
        assertEquals(
                3,
                resourceService.getAllResourceCategory(
                        EMPLOYEE_AUTHORISATION_TOKEN, EMPLOYEE_USERNAME).size());
    }

    @Test
    public void test30_getAllResourcesCategoryBasedResourcesWhenLoggedInUserIsInvalid() {
        assertEquals(
                0,
                resourceService.getResourcesBasedOnCategory(
                        "b789003ghjk678999900", EMPLOYEE_USERNAME, "Hardware")
                        .size());
    }

    @Test
    public void test30_getAllResourcesCategoryBasedResourcesWhenCategoryIsNull() {
        assertEquals(
                0,
                resourceService.getResourcesBasedOnCategory(
                        EMPLOYEE_AUTHORISATION_TOKEN, EMPLOYEE_USERNAME, null)
                        .size());
    }

    @Test
    public void test30_getAllResourcesCategoryBasedResourcesWhenCategoryIsEmpty() {
        assertEquals(
                0,
                resourceService.getResourcesBasedOnCategory(
                        EMPLOYEE_AUTHORISATION_TOKEN, EMPLOYEE_USERNAME, "")
                        .size());
    }

    @Test
    public void test30_getAllResourcesCategoryBasedResourcesWhenCategoryIsHardware() {
        assertEquals(
                5,
                resourceService.getResourcesBasedOnCategory(
                        EMPLOYEE_AUTHORISATION_TOKEN, EMPLOYEE_USERNAME,
                        "Hardware").size());
    }

    @Test
    public void test30_getAllResourcesCategoryBasedResourcesWhenCategoryIsSoftware() {
        assertEquals(
                3,
                resourceService.getResourcesBasedOnCategory(
                        EMPLOYEE_AUTHORISATION_TOKEN, EMPLOYEE_USERNAME,
                        "Software").size());
    }

    @Test
    public void test30_getAllResourcesCategoryBasedResourcesWhenCategoryIsNetwork() {
        assertEquals(
                1,
                resourceService.getResourcesBasedOnCategory(
                        EMPLOYEE_AUTHORISATION_TOKEN, EMPLOYEE_USERNAME,
                        "Network").size());
    }
}
