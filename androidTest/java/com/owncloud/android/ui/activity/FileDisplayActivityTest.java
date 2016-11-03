/**
 *   ownCloud Android client application
 *

 *   Copyright (C) 2016 ownCloud GmbH.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License version 2,
 *   as published by the Free Software Foundation.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.owncloud.android.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.os.Build;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.test.suitebuilder.annotation.LargeTest;
import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;

import com.owncloud.android.utils.AccountsManager;
import com.owncloud.android.lib.common.utils.Log_OC;
import com.owncloud.android.R;

import org.junit.Before;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class FileDisplayActivityTest {

    private Context targetContext = null;

    private static final String LOG_TAG = "FileSuite";
    private static final String nameFolder1 = "newFolder";
    private static final String nameFolder2 = "$%@??!";

    @Rule
    public ActivityTestRule<FileDisplayActivity> mActivityRule = new ActivityTestRule<FileDisplayActivity>(
            FileDisplayActivity.class) {

        @Override
        public void beforeActivityLaunched() {

            targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();

            Bundle arguments = InstrumentationRegistry.getArguments();

            String testUser = arguments.getString("TEST_USER");
            String testPassword = arguments.getString("TEST_PASSWORD");
            String testServerURL = arguments.getString("TEST_SERVER_URL");
            String testServerPort = arguments.getString("TEST_SERVER_PORT");

            String connectionString = getConnectionString(testServerURL, testServerPort);

            targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            AccountsManager.addAccount(targetContext, connectionString, testUser, testPassword);
        }
    };

    @Before
    public void init() {


        // UiDevice available form API level 17
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            try {
                if (!uiDevice.isScreenOn()) {
                    uiDevice.wakeUp();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  Create folder
     */
    @Test
    public void test1_create_folder()
            throws InterruptedException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        Log_OC.i(LOG_TAG, "Test Create Folder Correct Start");

        onView(withText(nameFolder1)).check(doesNotExist());

        onView(withId(R.id.fab_expand_menu_button)).perform(click());
        onView(withId(R.id.fab_mkdir)).perform(click());

        onView(withId(R.id.user_input)).perform(closeSoftKeyboard(),
                replaceText(nameFolder1), closeSoftKeyboard());

        onView(withText(R.string.common_ok)).perform(click());

        onView(withText(nameFolder1)).check(matches(isDisplayed()));

        Log_OC.i(LOG_TAG, "Test CreateFolder Correct Passed");

    }

    private String getConnectionString (String url, String port){

        if (port != null)
            return url+":"+port;
        else
            return url;
    }

    @After
    public void tearDown() throws Exception {
        //AccountsManager.deleteAllAccounts(targetContext);
    }
}

