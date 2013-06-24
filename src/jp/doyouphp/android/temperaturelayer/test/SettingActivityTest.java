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

import com.google.android.apps.mytracks.IntegerListPreference;

import jp.doyouphp.android.temperaturelayer.R;
import jp.doyouphp.android.temperaturelayer.SettingActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.test.ActivityInstrumentationTestCase2;

public class SettingActivityTest
        extends ActivityInstrumentationTestCase2<SettingActivity> {
    private SettingActivity mActivity;
    Class<?> mResource;
	private Map<String, String> mLayouts = new HashMap<String, String>();

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

        Context context = this.getInstrumentation().getTargetContext().getApplicationContext();
        try {
            mResource = context.getClassLoader().loadClass("jp.doyouphp.android.temperaturelayer.R");
        } catch (ClassNotFoundException e) {
            throw e;
        }
        
        mActivity = getActivity();
        mActivity.resetSetting();
        
        String[] entries = context.getResources().getStringArray(R.array.entries_layout);
        String[] values = context.getResources().getStringArray(R.array.values_layout);
        for (int i = 0; i < entries.length; i++) {
            mLayouts.put(values[i], entries[i]);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @SuppressWarnings("deprecation")
	public void testOnCreate() throws Exception {
        assertEquals(getStringFromR("app_name"), mActivity.getTitle().toString());

        /**
         * start on boot
         */
        Preference checkbox = mActivity.findPreference("key_start_on_boot");
        assertNotNull(checkbox);
        assertEquals(getStringFromR("label_start_on_boot"), checkbox.getTitle().toString());
        assertFalse(((CheckBoxPreference)checkbox).isChecked());
        assertEquals(getStringFromR("start_on_boot_no"), checkbox.getSummary().toString());

        /**
         * temperature unit
         */
        Preference temperature_unit = mActivity.findPreference("key_temperature_unit");
        assertNotNull(temperature_unit);
        assertEquals(getStringFromR("label_temperature_unit"), temperature_unit.getTitle().toString());
        assertEquals(
        		getStringFromR("string_degree", "", getStringFromR("default_temperature_unit")),
        		temperature_unit.getSummary().toString());
        assertEquals(getStringFromR("dialogtitle_temperature_unit"), ((ListPreference)temperature_unit).getDialogTitle().toString());
        
        /**
         * layout
         */
        Preference layout = mActivity.findPreference("key_layout");
        assertNotNull(layout);
        assertEquals(getStringFromR("label_layout"), layout.getTitle().toString());
        assertEquals(mLayouts.get(getStringFromR("default_layout")), layout.getSummary().toString());
        assertEquals(getStringFromR("dialogtitle_layout"), ((IntegerListPreference)layout).getDialogTitle().toString());
        
        /**
         * text size
         */
        Preference text_size = mActivity.findPreference("key_text_size");
        assertNotNull(text_size);
        assertEquals(getStringFromR("label_text_size"), text_size.getTitle().toString());
        assertEquals(
        		getStringFromR("size_unit", Integer.valueOf(getIntFromR("default_text_size"))),
        		text_size.getSummary().toString());
        assertEquals(getStringFromR("dialogtitle_text_size"), ((IntegerListPreference)text_size).getDialogTitle().toString());
        
        /**
         * text color
         */
        Preference color = mActivity.findPreference("key_color");
        assertNotNull(color);
        assertEquals(getStringFromR("label_color"), color.getTitle().toString());
        assertNull(color.getSummary());
    }

    /**
     * find @id by name from all declared @id
     */
    private int getIdFromR(String name) throws Exception {
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

    private String getStringFromR(String name, Object... args) {
        try {
            return mActivity.getString(getIdFromR(name), args);
        } catch (Exception e) {
            return "";
        }
    }
    
    private int getIntFromR(String name) {
        try {
        	return mActivity.getResources().getInteger(getIdFromR(name));
        } catch (Exception e) {
            return -1;
        }
    }
}
