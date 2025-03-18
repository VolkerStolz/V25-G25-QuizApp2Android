package com.example.quizapp2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QuizActivityTest {

    @Rule
    public ActivityScenarioRule<QuizActivity> activityScenarioRule =
            new ActivityScenarioRule<>(QuizActivity.class);

    @Test
    public void testScoreUpdatesCorrectly() {
        final String[] correctAnswer = new String[1];

        activityScenarioRule.getScenario().onActivity(activity -> {
            QuizFragment quizFragment = (QuizFragment) activity.getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container);

            if (quizFragment != null) {
                correctAnswer[0] = quizFragment.getCorrectAnswer();
            }
        });

        if (correctAnswer[0] != null) {
            onView(withText(correctAnswer[0])).perform(click());
        }

        onView(withId(R.id.scoreTextView)).check(matches(withText("Score: 1")));
    }
}
