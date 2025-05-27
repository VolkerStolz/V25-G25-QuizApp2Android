package com.example.quizapp2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(AndroidJUnit4.class)
public class PhotoTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // 🔥 Initialize Espresso Intents before the test
        Intents.init();
    }

    @After
    public void tearDown() {
        // 🔥 Release Espresso Intents after the test
        Intents.release();
    }

    @Test
    public void testImageAddedToRecyclerView() throws InterruptedException {
        // Wait for RecyclerView to appear
        waitForView(withId(R.id.recyclerView));
        // Get initial item count
        AtomicInteger initialCount = new AtomicInteger();
        activityScenarioRule.getScenario().onActivity(activity -> {
            RecyclerView rv = activity.findViewById(R.id.recyclerView);
            initialCount.set(rv.getAdapter().getItemCount());
        });

        // 🔥 Click "Add Photo" button (this starts `AddPhotoActivity`)
        onView(withId(R.id.buttonAddPhoto)).perform(click());

        // 🔥 Wait for `AddPhotoActivity` to launch
        Intents.intended(hasComponent(AddPhotoActivity.class.getName()));

        // 🔥 Simulate selecting an image
        Intent resultData = new Intent();
        Uri imageUri = Uri.parse("android.resource://com.example.quizapp2/drawable/gorilla"); // ✅ Correct URI
        resultData.setDataAndType(imageUri, "image/*");

        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasAction(Intent.ACTION_OPEN_DOCUMENT)).respondWith(result);

        // 🔥 Perform actions inside `AddPhotoActivity`
        onView(withId(R.id.editTextPhotoName)).perform(typeText("Gorilla Photo"));
        onView(withId(R.id.buttonChoosePhoto)).perform(click());  // Simulate choosing photo
        onView(withId(R.id.buttonSavePhoto)).perform(click());  // Save the photo

        // 🔥 Now go back to MainActivity
        // pressBack();

        // Wait for RecyclerView update
        waitForView(withId(R.id.recyclerView));

        // Verify RecyclerView count increased by 1
        onView(withId(R.id.recyclerView))
                .check(new RecyclerViewItemCountAssertion(initialCount.get() + 1));
    }


    // 🔥 Custom RecyclerView count assertion
    public static class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assert adapter != null;
            assertEquals(expectedCount, adapter.getItemCount());
        }
    }

    // 🔥 Helper method: Waits for a View to appear in the UI
    private void waitForView(final org.hamcrest.Matcher<View> matcher) {
        onView(matcher).check(matches(isDisplayed()));
    }

    // 🔥 Helper method: Retrieves LiveData values synchronously
    public static <T> T getOrAwaitValue(LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T t) {
                data[0] = t;
                liveData.removeObserver(this);
                latch.countDown();
            }
        };

        liveData.observeForever(observer);

        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw new InterruptedException("LiveData value was never set.");
        }

        return (T) data[0];
    }
}
