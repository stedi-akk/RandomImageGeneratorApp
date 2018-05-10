package com.stedi.randomimagegenerator.app.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.GeneratorTypeKt;
import com.stedi.randomimagegenerator.app.other.CommonKt;
import com.stedi.randomimagegenerator.app.view.activity.HomeActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.clickChildViewWithId;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.navigateInGenerationSteps;
import static com.stedi.randomimagegenerator.app.view.EspressoUtils.savePresetAndComebackToStep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ApplyGenerationTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    private final Resources res = InstrumentationRegistry.getTargetContext().getResources();

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

        GeneratorType selectedNonEffect = GeneratorType.COLORED_NOISE;
        GeneratorType selectedEffect = GeneratorType.TEXT_OVERLAY;

        onView(withId(R.id.choose_generator_fragment_recycler_view))
                .perform(actionOnItemAtPosition(GeneratorTypeKt.nonEffectOrdinal(selectedNonEffect), clickChildViewWithId(R.id.generator_type_item_card)));

        navigateInGenerationSteps("Generator", "Effect");

        onView(withId(R.id.choose_effect_fragment_recycler_view))
                .perform(actionOnItemAtPosition(GeneratorTypeKt.effectOrdinal(selectedEffect), clickChildViewWithId(R.id.generator_type_item_card)));

        navigateInGenerationSteps("Effect", "Size/count");

        onView(withId(R.id.choose_size_and_count_et_width)).perform(click(), clearText(), typeText("10"));
        onView(withId(R.id.choose_size_and_count_et_height)).perform(click(), clearText(), typeText("1000"));
        onView(withId(R.id.choose_size_and_count_et_count)).perform(click(), clearText(), typeText("69"));

        navigateInGenerationSteps("Size/count", "Quality");

        onView(allOf(withId(Bitmap.CompressFormat.WEBP.ordinal()), isDisplayed())).perform(click());

        navigateInGenerationSteps("Quality", "Summary");

        onView(withId(R.id.apply_generation_fragment_tv)).check(matches(withText(allOf(
                containsString(res.getString(R.string.generator_type_s, res.getString(CommonKt.nameRes(selectedNonEffect)))),
                containsString(res.getString(R.string.effect_type_s, res.getString(CommonKt.nameRes(selectedEffect)))),
                containsString(res.getString(R.string.width_s, "10")),
                containsString(res.getString(R.string.height_s, "1000")),
                containsString(res.getString(R.string.count_s, "69")),
                containsString(res.getString(R.string.quality_s_percent, Bitmap.CompressFormat.WEBP.name(), "100")
                )))));

        navigateInGenerationSteps("Summary", "Generator");

        selectedNonEffect = GeneratorType.FLAT_COLOR;

        onView(withId(R.id.choose_generator_fragment_recycler_view))
                .perform(actionOnItemAtPosition(GeneratorTypeKt.nonEffectOrdinal(selectedNonEffect), clickChildViewWithId(R.id.generator_type_item_card)));

        navigateInGenerationSteps("Generator", "Size/count");

        onView(withId(R.id.choose_size_and_count_et_height_range_from)).perform(click(), clearText(), typeText("11"));
        onView(withId(R.id.choose_size_and_count_et_height_range_to)).perform(click(), clearText(), typeText("222"));
        onView(withId(R.id.choose_size_and_count_et_height_range_step)).perform(click(), clearText(), typeText("33"));

        navigateInGenerationSteps("Size/count", "Summary");

        onView(withId(R.id.apply_generation_fragment_tv)).check(matches(withText(allOf(
                containsString(res.getString(R.string.generator_type_s, res.getString(CommonKt.nameRes(selectedNonEffect)))),
                containsString(res.getString(R.string.effect_type_s, res.getString(CommonKt.nameRes(selectedEffect)))),
                containsString(res.getString(R.string.width_s, "10")),
                containsString(res.getString(R.string.height_s, res.getString(R.string.from_s_to_s_step_s, "11", "222", "33"))),
                containsString(res.getString(R.string.quality_s_percent, Bitmap.CompressFormat.WEBP.name(), "100")
                )))));

        savePresetAndComebackToStep("name", "Summary");

        onView(withId(R.id.apply_generation_fragment_tv)).check(matches(withText(allOf(
                containsString(res.getString(R.string.name_s, "name")),
                containsString(res.getString(R.string.generator_type_s, res.getString(CommonKt.nameRes(selectedNonEffect)))),
                containsString(res.getString(R.string.effect_type_s, res.getString(CommonKt.nameRes(selectedEffect)))),
                containsString(res.getString(R.string.width_s, "10")),
                containsString(res.getString(R.string.height_s, res.getString(R.string.from_s_to_s_step_s, "11", "222", "33"))),
                containsString(res.getString(R.string.quality_s_percent, Bitmap.CompressFormat.WEBP.name(), "100")
                )))));
    }
}
