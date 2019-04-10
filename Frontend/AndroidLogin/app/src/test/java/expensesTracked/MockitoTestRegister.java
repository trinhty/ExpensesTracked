package expensesTracked;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class MockitoTestRegister {

    @Mock
    RegisterActivity activity;

    @Before
    public void init() {
        activity = new RegisterActivity();
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void breakdown() {
        activity = null;
    }
    @Mock
    RegisterActivity regActivity;

    /*@Test
    public void checkCredentials_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(activity.checkCredentials("name@email.com","qwertyuio12"));
    }
    @Test
    public void checkCredentials_CorrectEmailSubDomain_ReturnsTrue() {
        assertTrue(activity.checkCredentials("name@email.co.uk", "qwertyuio12"));
    }*/
    @Test
    public void checkCredentials_InvalidEmailNoTld_ReturnsFalse() {
        assertFalse(activity.checkCredentials("name@email","qwertyuio12"));
    }
    @Test
    public void checkCredentials_InvalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(activity.checkCredentials("name@email..com","qwertyuio12"));
    }
    @Test
    public void checkCredentials_InvalidEmailNoUsername_ReturnsFalse() {
        assertFalse(activity.checkCredentials("@email.com","qwertyuio12"));
    }
    @Test
    public void checkCredentials_EmptyString_ReturnsFalse() {
        assertFalse(activity.checkCredentials("","qwertyuio12"));
    }
    @Test
    public void checkCredentials_NullEmail_ReturnsFalse() {
        assertFalse(activity.checkCredentials(null, null));
    }
}
