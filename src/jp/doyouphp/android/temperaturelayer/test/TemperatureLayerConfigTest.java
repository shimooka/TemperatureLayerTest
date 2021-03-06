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

import jp.doyouphp.android.temperaturelayer.R;
import jp.doyouphp.android.temperaturelayer.config.TemperatureLayerConfig;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;

public class TemperatureLayerConfigTest extends InstrumentationTestCase {
    private TemperatureLayerConfig mConfig;
    private Context mContext;
    private SharedPreferences mPreferences;

    public TemperatureLayerConfigTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mContext = getInstrumentation().getTargetContext()
                .getApplicationContext();
        mConfig = new TemperatureLayerConfig(mContext);
        mConfig.reset();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void testInitial() {
        assertFalse(mConfig.isStartOnBoot());
        assertEquals(
                mContext.getResources().getString(
                        R.string.default_temperature_unit),
                mConfig.getTemperatureUnit());
        assertEquals(
                mContext.getResources().getInteger(R.integer.default_text_size),
                mConfig.getTextSize());
        assertEquals(
                mContext.getResources().getInteger(R.integer.default_layout),
                mConfig.getLayout());
        assertEquals(mContext.getResources().getColor(R.color.default_color),
                mConfig.getColor());
    }

    public void testStartOnBoot() {
        mPreferences.edit()
                .putBoolean(TemperatureLayerConfig.KEY_START_ON_BOOT, true)
                .commit();
        assertTrue(mConfig.isStartOnBoot());
    }

    public void testTemperatureUnit() {
        mPreferences.edit()
                .putString(TemperatureLayerConfig.KEY_TEMPERATURE_UNIT, "F")
                .commit();
        assertEquals("F", mConfig.getTemperatureUnit());
    }

    public void testTextSize() {
        mPreferences.edit().putInt(TemperatureLayerConfig.KEY_TEXT_SIZE, 20)
                .commit();
        assertEquals(20, mConfig.getTextSize());
    }

    public void testLayout() {
        mPreferences.edit().putInt(TemperatureLayerConfig.KEY_LAYOUT, 51)
                .commit();
        assertEquals(51, mConfig.getLayout());
    }

    public void testColor() {
        mPreferences.edit()
                .putInt(TemperatureLayerConfig.KEY_COLOR, 0x80000000).commit();
        assertEquals(0x80000000, mConfig.getColor());
    }
}