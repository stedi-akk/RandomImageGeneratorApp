package com.stedi.randomimagegenerator.app.view;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.GeneratorTypeKt;
import com.stedi.randomimagegenerator.app.view.activity.HomeActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.atRecyclerViewPosition;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.atView;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.clickChildViewWithId;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.savePresetAndComebackToStep;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ChooseGeneratorsTest {
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

        for (GeneratorType type : GeneratorType.Companion.getNON_EFFECT_TYPES()) {
            selectGeneratorSaveAndCheck(GeneratorTypeKt.nonEffectOrdinal(type), type.name());
        }
    }

    private void selectGeneratorSaveAndCheck(int generatorIndex, String name) {
        onView(withId(R.id.choose_generator_fragment_recycler_view))
                .perform(actionOnItemAtPosition(generatorIndex, clickChildViewWithId(R.id.generator_type_item_card)));

        savePresetAndComebackToStep(name, "Generator");

        onView(withId(R.id.choose_generator_fragment_recycler_view))
                .perform(scrollToPosition(generatorIndex))
                .check(matches(atRecyclerViewPosition(generatorIndex, atView(R.id.generator_type_item_selected, isDisplayed()))));
    }
}
