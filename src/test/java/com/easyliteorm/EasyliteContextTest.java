package com.easyliteorm;

import android.content.Context;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class EasyliteContextTest {

    @Test
    public void easyliteContextReturnAndroidApplicationTest (){
        Context result = EasyliteContext.getEasyliteContext();
        Assert.assertNotNull(result);
        Assert.assertEquals(EasyliteContext.getEasyliteContext(),result);
        Assert.assertTrue(result instanceof Context);
    }
}