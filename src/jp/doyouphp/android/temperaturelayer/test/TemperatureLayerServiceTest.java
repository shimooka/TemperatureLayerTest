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
