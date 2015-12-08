package navsingh.org.uk.easytracker.x13112406;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.Toast;

public class BarcodeReader extends Activity {
    private CameraPreview mPreview;
    private CameraManager mCameraManager;
    private HoverView mHoverView;
    //the variable mary gets the value from camera preview class
    private static String jerry;
    MyPreference pref;
     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Display display = getWindowManager().getDefaultDisplay();
		mHoverView = (HoverView)findViewById(R.id.hover_view);
		mHoverView.update(display.getWidth(), display.getHeight());
		
		mCameraManager = new CameraManager(this);
        mPreview = new CameraPreview(this, mCameraManager.getCamera());
        mPreview.setArea(mHoverView.getHoverLeft(), mHoverView.getHoverTop(), mHoverView.getHoverAreaWidth(), display.getWidth());
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        pref = new MyPreference(this);
        
        //getActionBar().hide();
       jerry = pref.getQRCODE();
      /*
        if (jerry != null) {
        	
    		Intent intent = new Intent(this, Congratulations.class);
    	    
    	    startActivity(intent);
        }
        
        */
        
	}
	
	
	
	@Override
    protected void onPause() {
        super.onPause();
        mPreview.onPause();
        mCameraManager.onPause(); 
    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mCameraManager.onResume();
		mPreview.setCamera(mCameraManager.getCamera());
		
	}
	
	
}
