package org.androidrobotics.analysis;

import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.annotations.OnCreate;
import org.androidrobotics.gen.ActivityDescriptor;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class ActivityAnalysisTest {

    public static final String TEST_NAME = "TestName";
    public static final int TEST_LAYOUT_ID = 123456;

    private ActivityAnalysis activityAnalysis;
    private ActivityDescriptor activityDescriptor;

    @Before
    public void setup() {
        activityAnalysis = new ActivityAnalysis();
        activityDescriptor = activityAnalysis.analyzeElement(new ClassAnalysisBridge(TestActivity.class));
    }

    @Test
    public void testActivityAnnotation() {
        assertEquals(TEST_NAME, activityDescriptor.getName());
    }

    @Test
    public void testLayoutAnnotation() {
        assertEquals(TEST_LAYOUT_ID, activityDescriptor.getLayout());
    }

    @Test
    public void testOnCreateMethod() {
        Collection<String> onCreateMethods = activityDescriptor.getMethods(OnCreate.class);

        String method = TestActivity.class.getMethods()[0].getName();

        assertEquals(1, onCreateMethods.size());
        assertEquals(method, onCreateMethods.iterator().next());
    }

    @Test
    public void testDelegateClassName() {
        assertEquals(TestActivity.class.getName(), activityDescriptor.getDelegateClass());
    }

    @Activity(ActivityAnalysisTest.TEST_NAME)
    @Layout(ActivityAnalysisTest.TEST_LAYOUT_ID)
    public class TestActivity {

        @OnCreate
        public void onCreate() {

        }
    }
}