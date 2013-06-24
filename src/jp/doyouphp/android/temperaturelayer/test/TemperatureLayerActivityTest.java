/**
 * Copyright 2013 Hideyuki SHIMOOKA <shimooka@doyouphp.jp>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.doyouphp.android.temperaturelayer.test;

import jp.doyouphp.android.temperaturelayer.SettingActivity;
import jp.doyouphp.android.temperaturelayer.TemperatureLayerActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.Button;

public class TemperatureLayerActivityTest
        extends ActivityInstrumentationTestCase2<TemperatureLayerActivity> {
    private TemperatureLayerActivity mActivity;
    Class<?> mResource;

    @SuppressLint("NewApi")
    public TemperatureLayerActivityTest() {
        super(TemperatureLayerActivity.class);
    }

    @SuppressLint("NewApi")
    public TemperatureLayerActivityTest(Class<TemperatureLayerActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        try {
            mResource = context.getClassLoader().loadClass("jp.doyouphp.android.temperaturelayer.R");
        } catch (ClassNotFoundException e) {
            throw e;
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testOnCreate() throws Exception {
        mActivity = getActivity();
        assertTrue(mActivity.getTitle().toString().matches(
                "^" + getStringFromR("app_name") + " ver(\\.[0-9]){3}$"));

        Button button = (Button)mActivity.findViewById(getIdFromR("StartButton"));
        assertEquals(getStringFromR("start_service"), button.getText().toString());
        assertTrue(button.isEnabled());

        button = (Button)mActivity.findViewById(getIdFromR("StopButton"));
        assertEquals(getStringFromR("stop_service"), button.getText().toString());
        assertFalse(button.isEnabled());
    }

    public void testButtonClick() throws Exception {
        mActivity = getActivity();
        final Button startButton = (Button)mActivity.findViewById(getIdFromR("StartButton"));
        final Button stopButton = (Button)mActivity.findViewById(getIdFromR("StopButton"));
        assertTrue(startButton.isEnabled());
        assertFalse(stopButton.isEnabled());

//      TouchUtils.clickView(this, startButton);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertFalse(startButton.isEnabled());
        assertTrue(stopButton.isEnabled());

//      TouchUtils.clickView(this, stopButton);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stopButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(startButton.isEnabled());
        assertFalse(stopButton.isEnabled());
    }

    public void testClickSettingMenu() throws Exception {
        ActivityMonitor monitor = getInstrumentation().addMonitor(SettingActivity.class.getName(), null, false);
        getInstrumentation().addMonitor(monitor);

        mActivity = getActivity();
        getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        getInstrumentation().invokeMenuActionSync(mActivity, getIdFromR("action_settings"), 0);

        Activity settingActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 2000);
        assertEquals(1, monitor.getHits());
        if (settingActivity != null) {
            settingActivity.finish();
        }
    }

    private int getIdFromR(String name) throws Exception {
    	// find @id by name from all declared @id
        for (Class<?> subclass: mResource.getClasses()) {
            try {
                return subclass.getField(name).getInt(null);
            } catch (NoSuchFieldException  e) {
                // skip
            } catch (Exception e) {
            	throw e;
            }
        }
        throw new NoSuchFieldException("key '" + name + "' not found");
    }

    private String getStringFromR(String name) {
        try {
            return mActivity.getText(getIdFromR(name)).toString();
        } catch (Exception e) {
            return "";
        }
    }
}
