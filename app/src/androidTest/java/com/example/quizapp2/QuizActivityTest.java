package com.example.quizapp2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

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
        // Finner korrekt svar fra activity først.
        final String[] correctAnswer = new String[1];

        activityScenarioRule.getScenario().onActivity(activity -> {
            correctAnswer[0] = activity.getCorrectAnswer();
        });


        if (correctAnswer[0] != null) {
            onView(withText(correctAnswer[0])).perform(click()); // Click the correct answer dynamically
        }

        // venter til UI har oppdatert før den sjekker scoren
        onView(withId(R.id.scoreTextView)).check(matches(withText("Score: 1")));
    }
}
