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

import android.content.Intent;
import android.test.ServiceTestCase;
import jp.doyouphp.android.temperaturelayer.service.TemperatureLayerService;

public class TemperatureLayerServiceTest extends ServiceTestCase<TemperatureLayerService> {
    private TemperatureLayerService mService;

    public TemperatureLayerServiceTest() {
        super(TemperatureLayerService.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mService = new TemperatureLayerService();
        TemperatureLayerService.isTest = true;
    }

    public void testStartingProperly() throws Exception {
        startService(new Intent(getContext(), TemperatureLayerService.class));
    }

    public void testCalculateTemperature() {
        assertEquals(0.0, mService.calculateTemperature(0, true));
        assertEquals(32.0, mService.calculateTemperature(0, false));
        assertEquals(32.1, mService.calculateTemperature(321, true));
        assertEquals(89.7, mService.calculateTemperature(321, false));
        assertEquals(-32.1, mService.calculateTemperature(-321, true));
        assertEquals(-25.8, mService.calculateTemperature(-321, false));
    }
}
