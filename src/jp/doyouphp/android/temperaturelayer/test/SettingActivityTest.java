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

import java.util.HashMap;
import java.util.Map;

import net.margaritov.preference.colorpicker.ColorPickerPanelView;
import net.margaritov.preference.colorpicker.ColorPickerPreference;

import com.google.android.apps.mytracks.IntegerListPreference;
import com.jayway.android.robotium.solo.Solo;

import jp.doyouphp.android.temperaturelayer.R;
import jp.doyouphp.android.temperaturelayer.SettingActivity;
import android.annotation.SuppressLint;
import android.graphics.Point;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;

public class SettingActivityTest
        extends AbstractTemperatureActivityTest<SettingActivity> {

    private Map<String, String> mLayouts = new HashMap<String, String>();
    private Solo solo;
    private static final int WAIT_MSEC = 500;

    @SuppressLint("NewApi")
    public SettingActivityTest() {
        super(SettingActivity.class);
    }

    @SuppressLint("NewApi")
    public SettingActivityTest(Class<SettingActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mActivity.resetSetting();
        solo = new Solo(getInstrumentation(), mActivity);

        String[] entries = mContext.getResources().getStringArray(R.array.entries_layout);
        String[] values = mContext.getResources().getStringArray(R.array.values_layout);
        for (int i = 0; i < entries.length; i++) {
            mLayouts.put(values[i], entries[i]);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testOnCreate() throws Exception {
        assertEquals(getStringFromR("app_name"), mActivity.getTitle().toString());
    }

    public void testStartOnBoot() throws Exception {
        @SuppressWarnings("deprecation")
        CheckBoxPreference preference = (CheckBoxPreference)mActivity.findPreference("key_start_on_boot");
        assertNotNull(preference);
        assertEquals(getStringFromR("label_start_on_boot"), preference.getTitle().toString());
        assertFalse(preference.isChecked());
        assertEquals(getStringFromR("start_on_boot_no"), preference.getSummary().toString());

        solo.clickOnCheckBox(0);
        solo.sleep(WAIT_MSEC);
        assertTrue(preference.isChecked());
        assertEquals(getStringFromR("start_on_boot_yes"), preference.getSummary().toString());

        solo.clickOnCheckBox(0);
        solo.sleep(WAIT_MSEC);
        assertFalse(preference.isChecked());
        assertEquals(getStringFromR("start_on_boot_no"), preference.getSummary().toString());
    }

    public void testTemperatureUnit() throws Exception {
        @SuppressWarnings("deprecation")
        ListPreference preference = (ListPreference)mActivity.findPreference("key_temperature_unit");
        assertNotNull(preference);
        assertEquals(getStringFromR("label_temperature_unit"), preference.getTitle().toString());
        assertEquals(
                getStringFromR("string_degree", "", getStringFromR("default_temperature_unit")),
                preference.getSummary().toString());
        assertEquals(getStringFromR("dialogtitle_temperature_unit"), preference.getDialogTitle().toString());

        // C (default) -> F
        solo.clickOnText(getStringFromR("label_temperature_unit"));
        assertEquals("C", preference.getValue());
        solo.clickInList(2);
        solo.waitForDialogToClose(WAIT_MSEC);
        assertEquals("°F", preference.getSummary().toString());

        // F -> C
        solo.clickOnText(getStringFromR("label_temperature_unit"));
        assertEquals("F", preference.getValue());
        solo.clickInList(1);
        solo.waitForDialogToClose(WAIT_MSEC);
        assertEquals("°C", preference.getSummary().toString());
    }

    public void testLayout() throws Exception {
        @SuppressWarnings("deprecation")
        ListPreference preference = (ListPreference)mActivity.findPreference("key_layout");
        assertNotNull(preference);
        assertEquals(getStringFromR("label_layout"), preference.getTitle().toString());
        assertEquals(mLayouts.get(getStringFromR("default_layout")), preference.getSummary().toString());
        assertEquals(getStringFromR("dialogtitle_layout"), ((IntegerListPreference)preference).getDialogTitle().toString());

        solo.clickOnText(getStringFromR("label_layout"));
        assertEquals(getStringFromR("default_layout"), preference.getValue());
        solo.clickInList(2);
        solo.waitForDialogToClose(WAIT_MSEC);
        assertEquals(mLayouts.get("53"), preference.getSummary().toString());

        solo.clickOnText(getStringFromR("label_layout"));
        assertEquals("53", preference.getValue());
        solo.clickInList(3);
        solo.waitForDialogToClose(WAIT_MSEC);
        assertEquals(mLayouts.get("83"), preference.getSummary().toString());
    }

    public void testTextSize() throws Exception {
        @SuppressWarnings("deprecation")
        ListPreference preference = (ListPreference)mActivity.findPreference("key_text_size");
        assertNotNull(preference);
        assertEquals(getStringFromR("label_text_size"), preference.getTitle().toString());
        assertEquals(
                getStringFromR("size_unit", Integer.valueOf(getIntFromR("default_text_size"))),
                preference.getSummary().toString());
        assertEquals(getStringFromR("dialogtitle_text_size"), ((IntegerListPreference)preference).getDialogTitle().toString());

        solo.clickOnText(getStringFromR("label_text_size"));
        assertEquals(Integer.toString(getIntFromR("default_text_size")), preference.getValue());
        solo.clickInList(2);
        solo.waitForDialogToClose(WAIT_MSEC);
        assertEquals("13 sp", preference.getSummary().toString());

        solo.clickOnText(getStringFromR("label_text_size"));
        assertEquals("13", preference.getValue());
        solo.clickInList(3);
        solo.waitForDialogToClose(WAIT_MSEC);
        assertEquals("15 sp", preference.getSummary().toString());
    }

    @SuppressLint("NewApi")
    public void testColor() throws Exception {
        @SuppressWarnings("deprecation")
        ColorPickerPreference preference = (ColorPickerPreference)mActivity.findPreference("key_color");
        assertNotNull(preference);
        assertEquals(getStringFromR("label_color"), preference.getTitle().toString());
        assertNull(preference.getSummary());

        solo.clickOnText(getStringFromR("label_color"));
        solo.waitForDialogToClose(WAIT_MSEC);

        Point size = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(size);
        solo.clickOnScreen(size.x / 3, size.y / 2);
        solo.waitForDialogToClose(WAIT_MSEC);
        solo.clickOnView(solo.getCurrentViews(ColorPickerPanelView.class).get(1));
        solo.waitForDialogToClose(WAIT_MSEC);
        assertNull(preference.getSummary());
    }
}
