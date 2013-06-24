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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.ContextThemeWrapper;

public abstract class AbstractTemperatureActivityTest<T extends Activity>
        extends ActivityInstrumentationTestCase2<T> {

    @SuppressLint("NewApi")
    public AbstractTemperatureActivityTest(Class<T> activityClass) {
        super(activityClass);
    }

    protected Class<?> mResource;
    protected T mActivity;
    protected Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mContext = this.getInstrumentation().getTargetContext().getApplicationContext();
        try {
            mResource = mContext.getClassLoader().loadClass("jp.doyouphp.android.temperaturelayer.R");
        } catch (ClassNotFoundException e) {
            throw e;
        }

        mActivity = (T)getActivity();
    }

    /**
     * find @id by name from all declared @id
     */
    protected int getIdFromR(String name) throws Exception {
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

    protected String getStringFromR(String name, Object... args) {
        try {
            return ((Context)mActivity).getString(getIdFromR(name), args);
        } catch (Exception e) {
            return "";
        }
    }

    protected int getIntFromR(String name) {
        try {
            return ((ContextThemeWrapper)mActivity).getResources().getInteger(getIdFromR(name));
        } catch (Exception e) {
            return -1;
        }
    }
}
