package me.argha.sustproject.test;

import me.argha.sustproject.MainActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


public class test extends ActivityInstrumentationTestCase2<MainActivity> {
  	private Solo solo;
  	
  	public test() {
		super(MainActivity.class);
  	}

  	public void setUp() throws Exception {
        super.setUp();
		solo = new Solo(getInstrumentation());
		getActivity();
  	}
  
   	@Override
   	public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
  	}
  
	public void testRun() {
        //Wait for activity: 'me.argha.sustproject.MainActivity'
		solo.waitForActivity(me.argha.sustproject.MainActivity.class, 2000);
        //Sleep for 13765 milliseconds
		solo.sleep(13765);
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
        //Sleep for 3604 milliseconds
		solo.sleep(3604);
        //Click on Add New Item FrameLayout
		solo.clickInRecyclerView(2, 0);
        //Wait for dialog
		solo.waitForDialogToOpen(5000);
        //Wait for dialog to close
		solo.waitForDialogToClose(5000);
        //Sleep for 23879 milliseconds
		solo.sleep(23879);
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
        //Sleep for 3783 milliseconds
		solo.sleep(3783);
        //Click on Log Out FrameLayout
		solo.clickInRecyclerView(6, 0);
        //Sleep for 24606 milliseconds
		solo.sleep(24606);
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
        //Sleep for 928 milliseconds
		solo.sleep(928);
	}
}
