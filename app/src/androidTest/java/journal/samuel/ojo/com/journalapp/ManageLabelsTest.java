package journal.samuel.ojo.com.journalapp;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.db.entity.JournalLabel;
import journal.samuel.ojo.com.journalapp.factory.JournalLabelViewModelFactory;
import journal.samuel.ojo.com.journalapp.util.SharedPreferencesUtil;
import journal.samuel.ojo.com.journalapp.viewmodel.JournalLabelListViewModel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ManageLabelsTest {

    private String userId;
    private String userIdSharedPrefsKey;
    private MainActivity activity;

    private List<JournalLabel> dbJournalLabels;
    private JournalDatabase journalDatabase;
    private JournalLabelListViewModel journalLabelListViewModel;
    private JournalLabelViewModelFactory journalLabelViewModelFactory;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        activity = mActivityTestRule.getActivity();

        SharedPreferencesUtil.purge(activity);
        userIdSharedPrefsKey = activity.getString(R.string.g_id);
        SharedPreferencesUtil.putString(activity, userIdSharedPrefsKey, "123456789");
        SharedPreferencesUtil.putString(activity, activity.getString(R.string.g_firstName), "Jack");
        SharedPreferencesUtil.putString(activity, activity.getString(R.string.g_lastName), "Jill");
        SharedPreferencesUtil.putString(activity, activity.getString(R.string.g_email), "jackj@fakemail.com");

        userId = SharedPreferencesUtil.getString(activity, userIdSharedPrefsKey);

        journalDatabase = JournalDatabase.getInstance(activity);
        journalDatabase.getJournalLabelDao().permanentlyDeleteAll();

        journalLabelViewModelFactory = new JournalLabelViewModelFactory(journalDatabase, userId);
        journalLabelListViewModel = ViewModelProviders.of(activity, journalLabelViewModelFactory).get(JournalLabelListViewModel.class);

        journalLabelListViewModel.getJournalLabels().observe(activity, new Observer<List<JournalLabel>>() {
            @Override
            public void onChanged(@Nullable List<JournalLabel> journalLabels) {
                dbJournalLabels = journalLabels;
            }
        });

        assertNull(dbJournalLabels);

    }

    @Test
    public void manageLabelsTest() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_labels), withContentDescription("Labels"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edtLabelName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        appCompatEditText.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edtLabelName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        appCompatEditText2.perform(scrollTo(), replaceText("poetry"), closeSoftKeyboard());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.btnAddLabel),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        appCompatImageButton.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.tvJournalLabel), withText("poetry"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rvJournalLabels),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("poetry")));

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.btnDelete),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rvJournalLabels),
                                        0),
                                1),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
