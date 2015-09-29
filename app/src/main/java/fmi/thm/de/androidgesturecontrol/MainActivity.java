package fmi.thm.de.androidgesturecontrol;

import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends ActionBarActivity {

    private XWalkView xWalkWebView;
    private View mDecorView;
    private boolean btn_fullscreen_clicked = false;
    private boolean btn_enable_gesture_clicked = false;
    private String rm_script = "document.body.removeChild(document.getElementById('video'));" +
            "document.body.removeChild(document.getElementById('canvas'));";
    private String add_script;

    private static  String TAG = "AGC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDecorView = getWindow().getDecorView();
        AssetManager am = getAssets();
/*        String[] list;
        try {
            list = am.list("js");
            for (String listelement : list) {
                Log.d(TAG, "AssetManager: found " + listelement);
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not list folders from AssetManager");
        }*/



        try {
            InputStream inputStream = am.open("gesture.js", am.ACCESS_BUFFER);

            if ( inputStream != null ) {
                add_script = convertStreamToString(inputStream);
                Log.v(TAG, "add_script length: " + add_script.length());
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }



        xWalkWebView = (XWalkView) findViewById(R.id.xwalkWebView);
        xWalkWebView.clearCache(true);
        xWalkWebView.load("https://slides.com", null);

        // turn on debugging
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);

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
            if (!btn_fullscreen_clicked){
                //item.setIcon(R.drawable.ic_fullscreen_exit_white_48dp);
                btn_fullscreen_clicked = true;
            } else {
                //item.setIcon(R.drawable.ic_fullscreen_white_48dp);
                btn_fullscreen_clicked = false;
            }
            hideSystemUI();
            return true;
        }

        if(id == R.id.action_enable_handgesture){
            if (!btn_enable_gesture_clicked){
                Toast toast = Toast.makeText(this, R.string.hand_gesture_enabled, Toast.LENGTH_SHORT);
                TextView toast_view = (TextView) toast.getView().findViewById(android.R.id.message);
                toast_view.setGravity(Gravity.CENTER);
                toast.show();

                item.setIcon(R.drawable.disable_hand_gesture);
                btn_enable_gesture_clicked = true;
                xWalkWebView.evaluateJavascript(add_script, null);
            } else {
                Toast toast = Toast.makeText(this, R.string.hand_gesture_disabled, Toast.LENGTH_SHORT);
                TextView toast_view = (TextView) toast.getView().findViewById(android.R.id.message);
                toast_view.setGravity(Gravity.CENTER);
                toast.show();
                item.setIcon(R.drawable.enable_hand_gesture);
                btn_enable_gesture_clicked = false;
                xWalkWebView.evaluateJavascript(rm_script, null);
            }
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
        if ((keyCode == KeyEvent.KEYCODE_BACK) && xWalkWebView.getNavigationHistory().canGoBack() ) {
            xWalkWebView.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
