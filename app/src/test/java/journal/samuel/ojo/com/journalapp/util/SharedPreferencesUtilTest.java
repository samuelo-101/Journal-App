package journal.samuel.ojo.com.journalapp.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SharedPreferencesUtilTest {

    @Mock
    private Activity mMockActivity;

    @Mock
    private SharedPreferences mMockSharedPreferences;

    @Mock
    private SharedPreferences.Editor mMockEditor;

    private final String test_key = "test_key";
    private final String test_value = "test_value";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mMockActivity.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(mMockSharedPreferences);
        when(mMockSharedPreferences.edit()).thenReturn(mMockEditor);
    }

    @Test
    public void test_putString_shouldSucceed() {
        assertNull(SharedPreferencesUtil.getString(mMockActivity, test_key));
        SharedPreferencesUtil.putString(mMockActivity, test_key, test_value);
        SharedPreferencesUtil.putString(mMockActivity, test_key, "another_test_value");
        verify(mMockEditor, times(2)).apply();
    }

    @Test
    public void test_putString__with_update_shouldSucceed() {
        assertNull(SharedPreferencesUtil.getString(mMockActivity, test_key));
        SharedPreferencesUtil.putString(mMockActivity, test_key, test_value);
        String another_test_value = "another_test_value";
        SharedPreferencesUtil.putString(mMockActivity, test_key, another_test_value);
        verify(mMockEditor, times(2)).apply();
    }

    @Test
    public void test_purge_shouldSucceed() {
        when(mMockSharedPreferences.edit().clear()).thenReturn(mMockEditor);
        SharedPreferencesUtil.putString(mMockActivity, test_key, test_value);
        SharedPreferencesUtil.purge(mMockActivity);
        verify(mMockEditor, times(1)).clear();
    }
}