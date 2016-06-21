package navsingh.org.uk.easytracker.x13112406;

import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

@SuppressLint("ViewConstructor")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private static final String TAG = "camera";
    private int mWidth, mHeight;
    private Context mContext;
    private MultiFormatReader mMultiFormatReader;
    private AlertDialog mDialog;
    private int mLeft, mTop, mAreaWidth, mAreaHeight;
    static Result result;
    public static  String roomID;
    BinaryBitmap bitmap;
    MyPreference pref;
    MySecondPreference spref;
   
    @SuppressWarnings("deprecation")
	public CameraPreview(Context context, Camera camera) {
        super(context);
        
        pref = new MyPreference(context);
        spref = new MySecondPreference(context);
        mCamera = camera;
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        Parameters params = mCamera.getParameters();
        mWidth = 640;
        mHeight = 480;
        params.setPreviewSize(mWidth, mHeight); 
        mCamera.setParameters(params);
        mMultiFormatReader = new MultiFormatReader();
        mDialog =  new AlertDialog.Builder(mContext).create();
    }

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        if (mHolder.getSurface() == null){
          return;
        }try {
            mCamera.stopPreview();
            } catch (Exception e){}

        try {
            mCamera.setPreviewCallback(mPreviewCallback);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
    
    public void setCamera(Camera camera) {
    	mCamera = camera;
    }
    
    public void onPause() {
    	if (mCamera != null) {
    		mCamera.setPreviewCallback(null);
    		mCamera.stopPreview();
    	}
    }
    
    private Camera.PreviewCallback mPreviewCallback = new PreviewCallback() {

        @SuppressWarnings("deprecation")
		@Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (mDialog.isShowing())
        		return;
        	LuminanceSource source = new PlanarYUVLuminanceSource(data, mWidth, mHeight, mLeft, mTop, mAreaWidth, mAreaHeight, false);
            bitmap = new BinaryBitmap(new HybridBinarizer(
              source));
      
            try {
				result = mMultiFormatReader.decode(bitmap, null);
				if (result != null) {
					// for saving the qr code data in a shared preference for later use
					spref.setSID(result.getText().toString());
					roomID = result.getText().toString();
					mDialog.setTitle("Result");
					mDialog.setMessage(roomID);
					mDialog.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					Intent intent = new Intent(mContext, Congratulations.class);
					mContext.startActivity(intent);
					}
					});
					mDialog.show();
					Intent intent = new Intent(mContext, Congratulations.class);
	        	    mContext.startActivity(intent);
	        	    ((Activity) mContext).finish();	
				}
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
        }
     };   
    
    public void setArea(int left, int top, int areaWidth, int width) {
    	double ratio = width / mWidth;
    	mLeft = (int) (left / (ratio + 1));
    	mTop = (int) (top / (ratio + 1));
    	mAreaHeight = mAreaWidth = mWidth - mLeft * 2;
    }

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
	}  
}