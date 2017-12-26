package de.vkoop.monit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatefulFilterTest {

    @Autowired
    StatefulFilter<String> filter;

    @Test
    public void testBlockOnlyOnce() {
        String key = "key1";
        boolean blocked = filter.blockAndCheckIfNew(key);
        assertTrue(blocked);

        boolean blockedSecondRequest = filter.blockAndCheckIfNew(key);
        assertFalse(blockedSecondRequest);
    }

    @Test
    public void testReset() {
        String key = "test1";
        filter.blockItem(key);

        boolean restored = filter.restore(key);
        assertTrue(restored);

        boolean secondTry = filter.restore(key);
        assertFalse(secondTry);
    }

}