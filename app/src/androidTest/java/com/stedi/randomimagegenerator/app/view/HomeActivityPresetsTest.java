package com.stedi.randomimagegenerator.app.view;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.view.activity.HomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.atRecyclerViewPosition;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.clickChildViewWithId;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.navigateInGenerationSteps;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.savePreset;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@android.support.test.filters.LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeActivityPresetsTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void testPresetSaveDeleteX2() {
        onView(withId(R.id.home_activity_empty_view))
                .check(matches(isDisplayed()));

        savePresetAndCheckIfExist("test 1", 0);
        savePresetAndCheckIfExist("test 2", 1);

        cancelDeletePresetAndCheckIfExist("test 1", 0);
        deletePresetAndCheckIfExist("test 1", 0);
        cancelDeletePresetAndCheckIfExist("test 2", 0);
        deletePresetAndCheckIfExist("test 2", 0);

        onView(withId(R.id.home_activity_empty_view))
                .check(matches(isDisplayed()));
    }

    private void savePresetAndCheckIfExist(String name, int position) {
        onView(allOf(withId(R.id.home_activity_fab), isDisplayed()))
                .perform(click());

        navigateInGenerationSteps("Generator", "Summary");

        savePreset(name);

        onView(withId(R.id.home_activity_recycler_view))
                .check(matches(atRecyclerViewPosition(position, hasDescendant(withText(name)))));
    }

    private void cancelDeletePresetAndCheckIfExist(String name, int position) {
        onView(withId(R.id.home_activity_recycler_view))
                .perform(actionOnItemAtPosition(position, clickChildViewWithId(R.id.preset_item_btn_delete)));

        Utils.sleep(500);

        onView(allOf(withId(android.R.id.button2), withText("Cancel")))
                .perform(scrollTo(), click());

        Utils.sleep(500);

        onView(withId(R.id.home_activity_recycler_view))
                .check(matches(atRecyclerViewPosition(position, hasDescendant(withText(name)))));
    }

    private void deletePresetAndCheckIfExist(String name, int position) {
        onView(withId(R.id.home_activity_recycler_view))
                .perform(actionOnItemAtPosition(position, clickChildViewWithId(R.id.preset_item_btn_delete)));

        Utils.sleep(500);

        onView(allOf(withId(android.R.id.button1), withText("OK")))
                .perform(scrollTo(), click());

        Utils.sleep(500);

        onView(withId(R.id.home_activity_recycler_view))
                .check(matches(not(atRecyclerViewPosition(position, hasDescendant(withText(name))))));
    }
}
