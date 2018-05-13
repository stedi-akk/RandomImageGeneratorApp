package com.stedi.randomimagegenerator.app.view;

import android.support.test.filters.LargeTest;
import android.support.annotation.IdRes;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.other.CommonKt;
import com.stedi.randomimagegenerator.app.view.activity.HomeActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.navigateInGenerationSteps;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.savePresetAndComebackToStep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.isEmptyString;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ChooseSizeAndCountTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    private final Integer[] WIDTH_HEIGHT_COUNT_IDS = new Integer[]{R.id.choose_size_and_count_et_width, R.id.choose_size_and_count_et_height, R.id.choose_size_and_count_et_count};
    private final Integer[] WIDTH_RANGE_IDS = new Integer[]{R.id.choose_size_and_count_et_width_range_from, R.id.choose_size_and_count_et_width_range_to, R.id.choose_size_and_count_et_width_range_step};
    private final Integer[] HEIGHT_RANGE_IDS = new Integer[]{R.id.choose_size_and_count_et_height_range_from, R.id.choose_size_and_count_et_height_range_to, R.id.choose_size_and_count_et_height_range_step};

    @BeforeClass
    public static void beforeClass() {
        TestUtils.deletePresetDatabase();
    }

    @Test
    public void chooseTest() {
        onView(withId(R.id.home_activity_empty_view))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.home_activity_fab), isDisplayed()))
                .perform(click());

        navigateInGenerationSteps("Generator", "Size/count");

        type(WIDTH_HEIGHT_COUNT_IDS[0], "100");
        type(WIDTH_HEIGHT_COUNT_IDS[1], "200");
        type(WIDTH_HEIGHT_COUNT_IDS[2], "300");

        savePresetAndComebackToStep("test", "Size/count");

        verifyText(WIDTH_HEIGHT_COUNT_IDS[0], "100");
        verifyText(WIDTH_HEIGHT_COUNT_IDS[1], "200");
        verifyText(WIDTH_HEIGHT_COUNT_IDS[2], "300");
        verifyEmpty(WIDTH_RANGE_IDS);
        verifyEmpty(HEIGHT_RANGE_IDS);
        CommonKt.sleep(500);

        type(WIDTH_RANGE_IDS[0], "100");

        savePresetAndComebackToStep("test", "Size/count");

        verifyText(WIDTH_RANGE_IDS[0], "100");
        verifyText(WIDTH_RANGE_IDS[1], "1");
        verifyText(WIDTH_RANGE_IDS[2], "1");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS[0]);
        verifyText(WIDTH_HEIGHT_COUNT_IDS[1], "200");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS[2]);
        verifyEmpty(HEIGHT_RANGE_IDS);
        CommonKt.sleep(500);

        type(HEIGHT_RANGE_IDS[0], "100");

        savePresetAndComebackToStep("test", "Size/count");

        verifyText(WIDTH_RANGE_IDS[0], "100");
        verifyText(WIDTH_RANGE_IDS[1], "1");
        verifyText(WIDTH_RANGE_IDS[2], "1");
        verifyText(HEIGHT_RANGE_IDS[0], "100");
        verifyText(HEIGHT_RANGE_IDS[1], "1");
        verifyText(HEIGHT_RANGE_IDS[2], "1");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS);
        CommonKt.sleep(500);

        type(WIDTH_HEIGHT_COUNT_IDS[2], "300");

        savePresetAndComebackToStep("test", "Size/count");

        verifyText(WIDTH_HEIGHT_COUNT_IDS[0], "1");
        verifyText(WIDTH_HEIGHT_COUNT_IDS[1], "1");
        verifyText(WIDTH_HEIGHT_COUNT_IDS[2], "300");
        verifyEmpty(WIDTH_RANGE_IDS);
        verifyEmpty(HEIGHT_RANGE_IDS);
        CommonKt.sleep(500);

        type(WIDTH_RANGE_IDS[2], "100");

        savePresetAndComebackToStep("test", "Size/count");

        verifyText(WIDTH_RANGE_IDS[0], "1");
        verifyText(WIDTH_RANGE_IDS[1], "1");
        verifyText(WIDTH_RANGE_IDS[2], "100");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS[0]);
        verifyText(WIDTH_HEIGHT_COUNT_IDS[1], "1");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS[2]);
        verifyEmpty(HEIGHT_RANGE_IDS);
        CommonKt.sleep(500);

        type(HEIGHT_RANGE_IDS[2], "100");

        savePresetAndComebackToStep("test", "Size/count");

        verifyText(WIDTH_RANGE_IDS[0], "1");
        verifyText(WIDTH_RANGE_IDS[1], "1");
        verifyText(WIDTH_RANGE_IDS[2], "100");
        verifyText(HEIGHT_RANGE_IDS[0], "1");
        verifyText(HEIGHT_RANGE_IDS[1], "1");
        verifyText(HEIGHT_RANGE_IDS[2], "100");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS);
        CommonKt.sleep(500);

        type(WIDTH_HEIGHT_COUNT_IDS[1], "100");

        savePresetAndComebackToStep("test", "Size/count");

        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS[0]);
        verifyText(WIDTH_HEIGHT_COUNT_IDS[1], "100");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS[2]);
        verifyText(WIDTH_RANGE_IDS[0], "1");
        verifyText(WIDTH_RANGE_IDS[1], "1");
        verifyText(WIDTH_RANGE_IDS[2], "100");
        verifyEmpty(HEIGHT_RANGE_IDS);
        CommonKt.sleep(500);

        type(WIDTH_HEIGHT_COUNT_IDS[0], "100");

        savePresetAndComebackToStep("test", "Size/count");

        verifyText(WIDTH_HEIGHT_COUNT_IDS[0], "100");
        verifyText(WIDTH_HEIGHT_COUNT_IDS[1], "100");
        verifyText(WIDTH_HEIGHT_COUNT_IDS[2], "1");
        verifyEmpty(WIDTH_RANGE_IDS);
        verifyEmpty(HEIGHT_RANGE_IDS);
        CommonKt.sleep(500);
    }

    @Test
    public void typeTest() {
        onView(withId(R.id.home_activity_empty_view))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.home_activity_fab), isDisplayed()))
                .perform(click());

        navigateInGenerationSteps("Generator", "Size/count");

        type(WIDTH_HEIGHT_COUNT_IDS[0], "100");
        type(WIDTH_HEIGHT_COUNT_IDS[1], "200");
        type(WIDTH_HEIGHT_COUNT_IDS[2], "300");
        verifyEmpty(WIDTH_RANGE_IDS);
        verifyEmpty(HEIGHT_RANGE_IDS);
        CommonKt.sleep(500);

        type(WIDTH_RANGE_IDS[0], "100");
        verifyText(WIDTH_RANGE_IDS[1], "1");
        verifyText(WIDTH_RANGE_IDS[2], "1");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS[0]);
        verifyText(WIDTH_HEIGHT_COUNT_IDS[1], "200");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS[2]);
        CommonKt.sleep(500);

        type(HEIGHT_RANGE_IDS[0], "100");
        verifyText(HEIGHT_RANGE_IDS[1], "1");
        verifyText(HEIGHT_RANGE_IDS[2], "1");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS);
        CommonKt.sleep(500);

        type(WIDTH_HEIGHT_COUNT_IDS[2], "300");
        verifyText(WIDTH_HEIGHT_COUNT_IDS[0], "1");
        verifyText(WIDTH_HEIGHT_COUNT_IDS[1], "1");
        verifyEmpty(WIDTH_RANGE_IDS);
        verifyEmpty(HEIGHT_RANGE_IDS);
        CommonKt.sleep(500);

        type(WIDTH_RANGE_IDS[2], "100");
        verifyText(WIDTH_RANGE_IDS[0], "1");
        verifyText(WIDTH_RANGE_IDS[1], "1");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS[0]);
        verifyText(WIDTH_HEIGHT_COUNT_IDS[1], "1");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS[2]);
        CommonKt.sleep(500);

        type(HEIGHT_RANGE_IDS[2], "100");
        verifyText(HEIGHT_RANGE_IDS[0], "1");
        verifyText(HEIGHT_RANGE_IDS[1], "1");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS);
        CommonKt.sleep(500);

        type(WIDTH_HEIGHT_COUNT_IDS[1], "100");
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS[0]);
        verifyEmpty(WIDTH_HEIGHT_COUNT_IDS[2]);
        verifyText(WIDTH_RANGE_IDS[2], "100");
        verifyText(WIDTH_RANGE_IDS[0], "1");
        verifyText(WIDTH_RANGE_IDS[1], "1");
        verifyEmpty(HEIGHT_RANGE_IDS);
        CommonKt.sleep(500);

        type(WIDTH_HEIGHT_COUNT_IDS[0], "100");
        verifyText(WIDTH_HEIGHT_COUNT_IDS[1], "100");
        verifyText(WIDTH_HEIGHT_COUNT_IDS[2], "1");
        verifyEmpty(WIDTH_RANGE_IDS);
        verifyEmpty(HEIGHT_RANGE_IDS);
        CommonKt.sleep(500);
    }

    private void type(@IdRes int id, String text) {
        onView(withId(id)).perform(scrollTo(), click(), clearText(), typeText(text));
    }

    private void verifyEmpty(@IdRes Integer... ids) {
        for (int id : ids) {
            onView(withId(id)).perform(scrollTo()).check(matches(withText(isEmptyString())));
        }
    }

    private void verifyText(@IdRes int id, String text) {
        onView(withId(id)).perform(scrollTo()).check(matches(withText(text)));
    }
}
