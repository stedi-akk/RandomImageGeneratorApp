package com.stedi.randomimagegenerator.app.view.activity;


import android.support.annotation.NonNull;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stedi.randomimagegenerator.app.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@android.support.test.filters.LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void testPresetSaveDeleteX2() {
        onView(withId(R.id.home_activity_empty_view))
                .check(matches(isDisplayed()));

        savePresetAndCheckIfExist("test 1", 0);
        savePresetAndCheckIfExist("test 2", 1);

        deletePresetAndCheckIfExist("test 1", 0);
        deletePresetAndCheckIfExist("test 2", 0);

        onView(withId(R.id.home_activity_empty_view))
                .check(matches(isDisplayed()));
    }

    private void savePresetAndCheckIfExist(String name, int position) {
        onView(allOf(withId(R.id.home_activity_fab), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.ms_stepNextButton), withText("Effect"),
                withParent(allOf(withId(R.id.ms_bottomNavigation),
                        withParent(withId(R.id.generation_steps_activity_stepper)))), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.ms_stepNextButton), withText("Size/count"),
                withParent(allOf(withId(R.id.ms_bottomNavigation),
                        withParent(withId(R.id.generation_steps_activity_stepper)))), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.ms_stepNextButton), withText("Quality"),
                withParent(allOf(withId(R.id.ms_bottomNavigation),
                        withParent(withId(R.id.generation_steps_activity_stepper)))), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.ms_stepNextButton), withText("Summary"),
                withParent(allOf(withId(R.id.ms_bottomNavigation),
                        withParent(withId(R.id.generation_steps_activity_stepper)))), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.apply_generation_fragment_btn_save), withText("(*) Save")))
                .perform(scrollTo(), click());

        onView(allOf(withId(R.id.edit_preset_name_dialog_et_name), isDisplayed()))
                .perform(replaceText(name), closeSoftKeyboard());

        onView(allOf(withId(android.R.id.button1), withText("OK")))
                .perform(scrollTo(), click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.home_activity_recycler_view))
                .check(matches(atPosition(position, hasDescendant(withText(name)))));
    }

    private void deletePresetAndCheckIfExist(String name, int position) {
        onView(withId(R.id.home_activity_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, clickChildViewWithId(R.id.preset_item_btn_delete)));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(android.R.id.button1), withText("OK")))
                .perform(scrollTo(), click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.home_activity_recycler_view))
                .check(matches(not(atPosition(position, hasDescendant(withText(name))))));
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.findViewById(id).performClick();
            }
        };
    }
}
