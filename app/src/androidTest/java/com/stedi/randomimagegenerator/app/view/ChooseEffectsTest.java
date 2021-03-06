package com.stedi.randomimagegenerator.app.view;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.GeneratorTypeKt;
import com.stedi.randomimagegenerator.app.view.activity.HomeActivity;

import org.hamcrest.Matcher;
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
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.navigateInGenerationSteps;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.savePresetAndComebackToStep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ChooseEffectsTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @BeforeClass
    public static void beforeClass() {
        TestUtils.deletePresetDatabase();
    }

    @Test
    public void test() {
        onView(withId(R.id.home_activity_empty_view))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.home_activity_fab), isDisplayed()))
                .perform(click());

        navigateInGenerationSteps("Generator", "Effect");

        checkEffectSelected(null);

        selectEffect(GeneratorType.MIRRORED);
        savePresetAndComebackToStep("name", "Effect");
        checkEffectSelected(GeneratorType.MIRRORED);

        selectEffect(GeneratorType.MIRRORED);
        savePresetAndComebackToStep("name", "Effect");
        checkEffectSelected(null);

        selectEffect(GeneratorType.TEXT_OVERLAY);
        savePresetAndComebackToStep("name", "Effect");
        checkEffectSelected(GeneratorType.TEXT_OVERLAY);

        selectEffect(GeneratorType.TEXT_OVERLAY);
        savePresetAndComebackToStep("name", "Effect");
        checkEffectSelected(null);

        selectEffect(GeneratorType.THRESHOLD);
        savePresetAndComebackToStep("name", "Effect");
        checkEffectSelected(GeneratorType.THRESHOLD);

        selectEffect(GeneratorType.THRESHOLD);
        savePresetAndComebackToStep("name", "Effect");
        checkEffectSelected(null);
    }

    private void selectEffect(GeneratorType effectType) {
        onView(withId(R.id.choose_effect_fragment_recycler_view))
                .perform(actionOnItemAtPosition(GeneratorTypeKt.effectOrdinal(effectType), clickChildViewWithId(R.id.generator_type_item_card)));
    }

    private void checkEffectSelected(GeneratorType effectType) {
        int indexShouldBeSelected = GeneratorTypeKt.effectOrdinal(effectType);

        for (int i = 0; i < GeneratorType.Companion.getEFFECT_TYPES().length; i++) {
            Matcher<View> isSelected = i == indexShouldBeSelected ? isDisplayed() : not(isDisplayed());
            onView(withId(R.id.choose_effect_fragment_recycler_view))
                    .perform(scrollToPosition(i))
                    .check(matches(atRecyclerViewPosition(i, atView(R.id.generator_type_item_selected, isSelected))));
        }
    }
}
