package com.evil.samplemvvm.ui.main;

import static org.mockito.Mockito.*;

import com.evil.samplemvvm.data.AppDataManager;
import com.evil.samplemvvm.data.preferences.SharedPreferenceManager;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {

    @Mock
    AppDataManager mAppDataManager;

    @Mock
    MainActivity mMainActivity;

    @Mock
    SharedPreferenceManager mSharedPreferenceManager;

    private MainViewModel mMainViewModel;

    @Before
    public void setUp() throws Exception {
        mMainViewModel = new MainViewModel(mAppDataManager);
    }

    @Test
    public void testRetrieveSampleString() {
        //Given
        String str = "Hello World";
//        Truth.assert_().that(str1).

    }
}