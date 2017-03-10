package com.goteborgstad;

import android.content.Context;

import static junit.framework.Assert.assertEquals;
public class ExampleInstrumentedTest {
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.goteborgstad", appContext.getPackageName());
    }
}
