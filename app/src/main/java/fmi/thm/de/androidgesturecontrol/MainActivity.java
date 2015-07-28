package fmi.thm.de.androidgesturecontrol;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import edu.washington.cs.touchfreelibrary.sensors.ClickSensor;
import edu.washington.cs.touchfreelibrary.sensors.CameraGestureSensor;

public class MainActivity extends ActionBarActivity implements CameraGestureSensor.Listener {

    private WebView mWebView;

    private View mDecorView;

    private static  String TAG = "AGC";

    /** True if the openCV library has been initiated.
     *  False otherwise*/
    private boolean mOpenCVInitiated = false;

    /** Sensor that detects gestures. Calls the appropriate
     *  functions when the motions are recognized. */
    private CameraGestureSensor mGestureSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize openCV
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);

        mGestureSensor = new CameraGestureSensor(this);
        mGestureSensor.addGestureListener(this);

        mDecorView = getWindow().getDecorView();

        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        mWebView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = mWebView.getSettings();
        // Enable Javascript
        webSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl("http://drive.google.com/");

        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0) {
                    showSystemUI();
                    Toast toast = Toast.makeText(MainActivity.this, R.string.fullscreen_exit, Toast.LENGTH_SHORT);
                    TextView toast_view = (TextView) toast.getView().findViewById(android.R.id.message);
                    toast_view.setGravity(Gravity.CENTER);
                    toast.show();
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fullscreen) {
            Toast toast = Toast.makeText(this, R.string.fullscreen_toast, Toast.LENGTH_SHORT);
            TextView toast_view = (TextView) toast.getView().findViewById(android.R.id.message);
            toast_view.setGravity(Gravity.CENTER);
            toast.show();

            Log.d(TAG, "Button clicked");

            hideSystemUI();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        mDecorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    /** OpenCV library initialization. */
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCVInitiated = true;
                    CameraGestureSensor.loadLibrary();
                    mGestureSensor.start();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    /** Moves onto the next screen if an upwards gesture is received. */
    @Override
    public void onGestureUp(CameraGestureSensor caller, long gestureLength) {
        Log.e(TAG, "Up gesture detected");
    }

    /** Moves onto the next screen if an downwards gesture is received. */
    @Override
    public void onGestureDown(CameraGestureSensor caller, long gestureLength) {
        Log.e(TAG, "Down gesture detected");
    }

    /** Moves onto the next screen if an leftwards gesture is received. */
    @Override
    public void onGestureLeft(CameraGestureSensor caller, long gestureLength) {
        Log.e(TAG, "Left gesture detected");
    }

    /** Moves onto the next screen if an rightwards gesture is received. */
    @Override
    public void onGestureRight(CameraGestureSensor caller, long gestureLength) {
        Log.e(TAG, "Right gesture detected");
    }

    /** Called when the activity is resumed. The gesture detector is initialized. */
    @Override
    public void onResume() {
        super.onResume();
        if(!mOpenCVInitiated)
            return;
        mGestureSensor.start();
    }

    /** Called when the activity is paused. The gesture detector is stopped
     *  so that the camera is no longer working to recognize gestures. */
    @Override
    public void onPause() {
        super.onPause();
        if(!mOpenCVInitiated)
            return;
        mGestureSensor.stop();
    }

}
