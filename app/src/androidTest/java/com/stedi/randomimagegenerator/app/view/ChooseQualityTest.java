package com.stedi.randomimagegenerator.app.view;

import android.graphics.Bitmap;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.other.CommonKt;
import com.stedi.randomimagegenerator.app.view.activity.HomeActivity;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.clickLocation;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.navigateInGenerationSteps;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.savePresetAndComebackToStep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ChooseQualityTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

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

        navigateInGenerationSteps("Generator", "Quality");

        // change formats
        selectFormat(Bitmap.CompressFormat.PNG);
        selectFormat(Bitmap.CompressFormat.JPEG);
        selectFormat(Bitmap.CompressFormat.WEBP);
        selectFormat(Bitmap.CompressFormat.PNG);
        selectFormat(Bitmap.CompressFormat.JPEG);
        selectFormat(Bitmap.CompressFormat.WEBP);

        // check changed
        checkFormatIs(Bitmap.CompressFormat.WEBP);
        checkQualityValueIs(100);

        // change to JPG 95
        selectFormat(Bitmap.CompressFormat.JPEG);
        clickQualityValuePicker(5, GeneralLocation.TOP_CENTER);

        savePresetAndComebackToStep("name", "Quality");

        // assert JPG 95
        checkFormatIs(Bitmap.CompressFormat.JPEG);
        checkQualityValueIs(95);

        // change to PNG and check 100 quality value
        selectFormat(Bitmap.CompressFormat.PNG);
        checkQualityValueIs(100);

        // try to change quality value
        clickQualityValuePicker(2, GeneralLocation.TOP_CENTER);
        clickQualityValuePicker(2, GeneralLocation.CENTER);
        clickQualityValuePicker(2, GeneralLocation.BOTTOM_CENTER);

        // check that format and quality valued did not change
        checkFormatIs(Bitmap.CompressFormat.PNG);
        checkQualityValueIs(100);

        savePresetAndComebackToStep("name", "Quality");

        // assert PNG 100
        checkFormatIs(Bitmap.CompressFormat.PNG);
        checkQualityValueIs(100);

        // change to WEBP 5
        selectFormat(Bitmap.CompressFormat.WEBP);
        clickQualityValuePicker(6, GeneralLocation.BOTTOM_CENTER);

        savePresetAndComebackToStep("name", "Quality");

        // assert WEBP 5
        checkFormatIs(Bitmap.CompressFormat.WEBP);
        checkQualityValueIs(5);
    }

    private void selectFormat(Bitmap.CompressFormat format) {
        onView(allOf(withId(format.ordinal()), isDisplayed())).perform(click());
    }

    private void checkFormatIs(Bitmap.CompressFormat format) {
        for (Bitmap.CompressFormat fm : Bitmap.CompressFormat.values()) {
            Matcher<View> checked = fm == format ? isChecked() : not(isChecked());
            onView(allOf(withId(fm.ordinal()), isDisplayed())).check(matches(checked));
        }
    }

    private void clickQualityValuePicker(int times, GeneralLocation location) {
        for (int i = 0; i < times; i++) {
            onView(withClassName(Matchers.equalTo(NumberPicker.class.getName()))).perform(clickLocation(location));
            CommonKt.sleep(100);
        }
    }

    private void checkQualityValueIs(int value) {
        onView(allOf(instanceOf(EditText.class), withParent(withClassName(Matchers.equalTo(NumberPicker.class.getName())))))
                .check(matches(withText(String.valueOf(value))));
    }
}
