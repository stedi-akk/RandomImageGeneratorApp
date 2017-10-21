package com.stedi.randomimagegenerator.app.view;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.view.activity.HomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.atRecyclerViewPosition;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.atView;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.clickChildViewWithId;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.navigateInGenerationSteps;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.savePresetAndComebackToStep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@android.support.test.filters.LargeTest
@RunWith(AndroidJUnit4.class)
public class ChooseEffectsTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void chooseTest() {
        onView(withId(R.id.home_activity_empty_view))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.home_activity_fab), isDisplayed()))
                .perform(click());

        navigateInGenerationSteps("Generator", "Effect");

        onView(withId(R.id.choose_effect_fragment_recycler_view))
                .check(matches(atRecyclerViewPosition(0, atView(R.id.generator_type_item_selected, not(isDisplayed())))));
        onView(withId(R.id.choose_effect_fragment_recycler_view))
                .check(matches(atRecyclerViewPosition(1, atView(R.id.generator_type_item_selected, not(isDisplayed())))));

        onView(withId(R.id.choose_effect_fragment_recycler_view))
                .perform(actionOnItemAtPosition(0, clickChildViewWithId(R.id.generator_type_item_card)));

        savePresetAndComebackToStep("step", "Effect");

        onView(withId(R.id.choose_effect_fragment_recycler_view))
                .check(matches(atRecyclerViewPosition(0, atView(R.id.generator_type_item_selected, isDisplayed()))));
        onView(withId(R.id.choose_effect_fragment_recycler_view))
                .check(matches(atRecyclerViewPosition(1, atView(R.id.generator_type_item_selected, not(isDisplayed())))));

        onView(withId(R.id.choose_effect_fragment_recycler_view))
                .perform(actionOnItemAtPosition(0, clickChildViewWithId(R.id.generator_type_item_card)));

        savePresetAndComebackToStep("step", "Effect");

        onView(withId(R.id.choose_effect_fragment_recycler_view))
                .check(matches(atRecyclerViewPosition(0, atView(R.id.generator_type_item_selected, not(isDisplayed())))));
        onView(withId(R.id.choose_effect_fragment_recycler_view))
                .check(matches(atRecyclerViewPosition(1, atView(R.id.generator_type_item_selected, not(isDisplayed())))));

        onView(withId(R.id.choose_effect_fragment_recycler_view))
                .perform(actionOnItemAtPosition(1, clickChildViewWithId(R.id.generator_type_item_card)));

        savePresetAndComebackToStep("step", "Effect");

        onView(withId(R.id.choose_effect_fragment_recycler_view))
                .check(matches(atRecyclerViewPosition(0, atView(R.id.generator_type_item_selected, not(isDisplayed())))));
        onView(withId(R.id.choose_effect_fragment_recycler_view))
                .check(matches(atRecyclerViewPosition(1, atView(R.id.generator_type_item_selected, isDisplayed()))));
    }
}
