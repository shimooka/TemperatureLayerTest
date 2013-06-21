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
	
	public void testOnCreate() {
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
	
	public void testButtonClick() {
		mActivity = getActivity();
		final Button startButton = (Button)mActivity.findViewById(getIdFromR("StartButton"));
		final Button stopButton = (Button)mActivity.findViewById(getIdFromR("StopButton"));
		assertTrue(startButton.isEnabled());
		assertFalse(stopButton.isEnabled());

//		TouchUtils.clickView(this, startButton);
		mActivity.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            startButton.performClick();
	        }
	    });
		getInstrumentation().waitForIdleSync();
		assertFalse(startButton.isEnabled());
		assertTrue(stopButton.isEnabled());
		
//		TouchUtils.clickView(this, stopButton);
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
	
	public void testClickSettingMenu() {
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

    private int getIdFromR(String name) {
    	for (Class<?> subclass: mResource.getClasses()) {
	        try {
	            return subclass.getField(name).getInt(null);
	        } catch (Exception e) {
	            // nop
	        }
    	}
    	return -1;
    }
    
    private String getStringFromR(String name) {
        try {
            return mActivity.getText(getIdFromR(name)).toString();
        } catch (Exception e) {
            return "";
        }
    }
}
