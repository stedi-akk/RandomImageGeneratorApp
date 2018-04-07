package com.stedi.randomimagegenerator.app.view;

import android.graphics.Bitmap;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.NumberPicker;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.view.activity.HomeActivity;

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
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.navigateInGenerationSteps;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.savePresetAndComebackToStep;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.setPickerNumber;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@android.support.test.filters.LargeTest
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

        onView(allOf(withId(Bitmap.CompressFormat.JPEG.ordinal()), isDisplayed()))
                .perform(click());

        onView(withClassName(Matchers.equalTo(NumberPicker.class.getName())))
                .perform(setPickerNumber(50));

        savePresetAndComebackToStep("test", "Quality");

        onView(allOf(withId(Bitmap.CompressFormat.JPEG.ordinal()), isDisplayed()))
                .check(matches(isChecked()));
        onView(allOf(withId(Bitmap.CompressFormat.PNG.ordinal()), isDisplayed()))
                .check(matches(not(isChecked())));
        onView(allOf(withId(Bitmap.CompressFormat.WEBP.ordinal()), isDisplayed()))
                .check(matches(not(isChecked())));
    }
}
