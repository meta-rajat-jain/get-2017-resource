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

    @Test
    public void test30_getAllResourcesCategoryWhenLoggedInUserIsvalid() {
        assertEquals(3, resourceService.getAllResourceCategory().size());
    }

    @Test
    public void test30_getAllResourcesCategoryBasedResourcesWhenCategoryIsNull() {
        assertEquals(0, resourceService.getResourcesBasedOnCategory(null)
                .size());
    }

    @Test
    public void test30_getAllResourcesCategoryBasedResourcesWhenCategoryIsEmpty() {
        assertEquals(0, resourceService.getResourcesBasedOnCategory("").size());
    }

    @Test
    public void test30_getAllResourcesCategoryBasedResourcesWhenCategoryIsHardware() {
        assertEquals(4, resourceService.getResourcesBasedOnCategory("Hardware")
                .size());
    }

    @Test
    public void test30_getAllResourcesCategoryBasedResourcesWhenCategoryIsSoftware() {
        assertEquals(2, resourceService.getResourcesBasedOnCategory("Software")
                .size());
    }

    @Test
    public void test30_getAllResourcesCategoryBasedResourcesWhenCategoryIsNetwork() {
        assertEquals(1, resourceService.getResourcesBasedOnCategory("Network")
                .size());
    }
}
