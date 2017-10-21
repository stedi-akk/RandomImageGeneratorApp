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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.atRecyclerViewPosition;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.atView;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.clickChildViewWithId;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.navigateInGenerationSteps;
import static org.hamcrest.Matchers.allOf;

@android.support.test.filters.LargeTest
@RunWith(AndroidJUnit4.class)
public class ChooseGeneratorsTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void chooseTest() {
        onView(withId(R.id.home_activity_empty_view))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.home_activity_fab), isDisplayed()))
                .perform(click());

        selectGeneratorSaveAndCheck(1, "rectangles");
        selectGeneratorSaveAndCheck(0, "circles");
        selectGeneratorSaveAndCheck(2, "pixels");
        selectGeneratorSaveAndCheck(3, "flat color");
        selectGeneratorSaveAndCheck(4, "noise");
    }

    private void selectGeneratorSaveAndCheck(int generatorIndex, String name) {
        onView(withId(R.id.choose_generator_fragment_recycler_view))
                .perform(actionOnItemAtPosition(generatorIndex, clickChildViewWithId(R.id.generator_type_item_card)));

        navigateInGenerationSteps("Generator", "Summary");

        onView(allOf(withId(R.id.apply_generation_fragment_btn_save), withText("(*) Save")))
                .perform(scrollTo(), click());

        onView(allOf(withId(R.id.edit_preset_name_dialog_et_name), isDisplayed()))
                .perform(replaceText(name), closeSoftKeyboard());

        onView(allOf(withId(android.R.id.button1), withText("OK")))
                .perform(scrollTo(), click());

        Utils.sleep(1000);

        onView(withId(R.id.home_activity_recycler_view))
                .perform(actionOnItemAtPosition(0, click()));

        navigateInGenerationSteps("Summary", "Generator");

        onView(withId(R.id.choose_generator_fragment_recycler_view))
                .check(matches(atRecyclerViewPosition(generatorIndex, atView(R.id.generator_type_item_selected, isDisplayed()))));
    }
}
