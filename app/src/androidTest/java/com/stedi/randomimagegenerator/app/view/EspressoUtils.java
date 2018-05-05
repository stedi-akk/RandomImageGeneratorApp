package com.stedi.randomimagegenerator.app.view;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.other.CommonKt;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

final class EspressoUtils {
    private static List<String> GENERATION_STEPS = Arrays.asList("Generator", "Effect", "Size/count", "Quality", "Summary", "Configure");

    private EspressoUtils() {
    }

    @NonNull
    static ViewAction clickLocation(@NonNull GeneralLocation location) {
        return actionWithAssertions(
                new GeneralClickAction(Tap.SINGLE, location, Press.FINGER,
                        InputDevice.SOURCE_UNKNOWN, MotionEvent.BUTTON_PRIMARY));
    }

    @NonNull
    static ViewAction clickChildViewWithId(@IdRes final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
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

    @NonNull
    static Matcher<View> atRecyclerViewPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + " ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    @NonNull
    static Matcher<View> atView(@IdRes final int id, @NonNull final Matcher<View> viewMatcher) {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has child with id " + id + " ");
                viewMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final View view) {
                View child = view.findViewById(id);
                return child != null && viewMatcher.matches(child);
            }
        };
    }

    static void navigateInGenerationSteps(@NonNull String from, @NonNull String to) {
        int fromIndex = GENERATION_STEPS.indexOf(from);
        int toIndex = GENERATION_STEPS.indexOf(to);
        if (fromIndex < 0 || toIndex < 0 || fromIndex == toIndex)
            return;
        if (fromIndex < toIndex && toIndex != 5) {
            for (int i = fromIndex + 1; i <= toIndex; i++) {
                onView(allOf(withId(R.id.ms_stepNextButton), withText(GENERATION_STEPS.get(i)),
                        withParent(allOf(withId(R.id.ms_bottomNavigation),
                                withParent(withId(R.id.generation_steps_activity_stepper)))), isDisplayed()))
                        .perform(click());
            }
        } else {
            fromIndex = toIndex == 5 || fromIndex == 4 ? 6 : fromIndex;
            for (int i = fromIndex - 1; i >= toIndex; i--) {
                if (i == 3 || i == 4)
                    continue;
                onView(allOf(withId(R.id.ms_stepPrevButton), withText(GENERATION_STEPS.get(i)),
                        withParent(allOf(withId(R.id.ms_bottomNavigation),
                                withParent(withId(R.id.generation_steps_activity_stepper)))), isDisplayed()))
                        .perform(click());
            }
        }
    }

    static void savePresetAndComebackToStep(@NonNull String name, @NonNull String step) {
        navigateInGenerationSteps(step, GENERATION_STEPS.get(GENERATION_STEPS.size() - 2));

        savePreset(name);

        onView(withId(R.id.home_activity_recycler_view))
                .perform(actionOnItemAtPosition(0, click()));

        navigateInGenerationSteps(GENERATION_STEPS.get(GENERATION_STEPS.size() - 2), step);
    }

    static void savePreset(@NonNull String name) {
        onView(allOf(withId(R.id.apply_generation_fragment_btn_save), withText("(*) Save")))
                .perform(scrollTo(), click());

        onView(allOf(withId(R.id.edit_preset_name_dialog_et_name), isDisplayed()))
                .perform(replaceText(name), closeSoftKeyboard());

        onView(allOf(withId(android.R.id.button1), withText("OK")))
                .perform(scrollTo(), click());

        CommonKt.sleep(1000);
    }
}
