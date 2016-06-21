package navsingh.org.uk.easytracker.x13112406;
import android.content.Context;
import android.hardware.Camera;
import android.widget.Toast;

public class CameraManager {
	private Camera mCamera;
	private Context mContext;

	public CameraManager(Context context) {
		mContext = context;
        mCamera = getCameraInstance();
	}

	public Camera getCamera() {
		return mCamera;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}
	
	public void onPause() {
		releaseCamera();
	}
	
	public void onResume() {
		if (mCamera == null) {
			mCamera = getCameraInstance();
		}
		Toast.makeText(mContext, "preview size = " + mCamera.getParameters().getPreviewSize().width + 
				", " + mCamera.getParameters().getPreviewSize().height, Toast.LENGTH_LONG).show(); 
	}
	
	private static Camera getCameraInstance(){
	    Camera c = null;
	    try { c = Camera.open(); }
	    catch (Exception e){}
	    return c; 
	}
}