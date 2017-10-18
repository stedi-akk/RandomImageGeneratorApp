package com.stedi.randomimagegenerator.app.view.activity;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.app.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void testPresetSave() {
        onView(withId(R.id.home_activity_empty_view))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.home_activity_fab), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.ms_stepNextButton), withText("Effect"),
                withParent(allOf(withId(R.id.ms_bottomNavigation),
                        withParent(withId(R.id.generation_steps_activity_stepper)))),
                isDisplayed())).perform(click());

        onView(allOf(withId(R.id.ms_stepNextButton), withText("Size/count"),
                withParent(allOf(withId(R.id.ms_bottomNavigation),
                        withParent(withId(R.id.generation_steps_activity_stepper)))),
                isDisplayed())).perform(click());

        onView(allOf(withId(R.id.ms_stepNextButton), withText("Quality"),
                withParent(allOf(withId(R.id.ms_bottomNavigation),
                        withParent(withId(R.id.generation_steps_activity_stepper)))),
                isDisplayed())).perform(click());

        onView(allOf(withId(R.id.ms_stepNextButton), withText("Summary"),
                withParent(allOf(withId(R.id.ms_bottomNavigation),
                        withParent(withId(R.id.generation_steps_activity_stepper)))),
                isDisplayed())).perform(click());

        onView(allOf(withId(R.id.apply_generation_fragment_btn_save), withText("(*) Save")))
                .perform(scrollTo(), click());

        onView(allOf(withId(R.id.edit_preset_name_dialog_et_name), isDisplayed()))
                .perform(replaceText("test"), closeSoftKeyboard());

        onView(allOf(withId(android.R.id.button1), withText("OK")))
                .perform(scrollTo(), click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.preset_item_tv_name), isDisplayed()))
                .check(matches(withText("test")));
    }
}
