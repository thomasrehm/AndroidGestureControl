package fmi.thm.de.androidgesturecontrol;

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

public class MainActivity extends ActionBarActivity {

    private XWalkView xWalkWebView;
    private View mDecorView;
    private boolean btn_fullscreen_clicked = false;
    private boolean btn_enable_gesture_clicked = false;
    private String rm_script = "document.body.removeChild(document.getElementById('video'));" +
            "document.body.removeChild(document.getElementById('canvas'));";
    private String add_script = "var vid = document.createElement('video'),\n" +
            "can = document.createElement('canvas');\n" +
            "vid.setAttribute('id','video');\n" +
            "vid.setAttribute('width', '300');\n" +
            "vid.setAttribute('style', 'display:none');\n" +
            "vid.setAttribute('autoplay', 'autoplay');\n" +
            "can.setAttribute('id', 'canvas');\n" +
            "can.setAttribute('style', 'width:300px;display:none;');\n" +
            "\n" +
            "document.body.appendChild(vid);\n" +
            "document.body.appendChild(can);\n" +
            "console.log('injected video and canvas element');\n" +
            "setTimeout(gesture, 1000);\n" +
            "function gesture(){\n" +
            "    \"use strict\";\n" +
            "\n" +
            "    var doc = window.document,\n" +
            "        draw,\n" +
            "        video = doc.getElementById('video'),\n" +
            "        canvas = doc.getElementById('canvas'),\n" +
            "        _ = canvas.getContext('2d'),\n" +
            "        // ccanvas = doc.getElementById('comp'),\n" +
            "        // c_ = ccanvas.getContext('2d'),\n" +
            "        compression = 5,\n" +
            "        height = 0,\n" +
            "        width = 0,\n" +
            "        huemin = 0.0,\n" +
            "        huemax = 0.1,\n" +
            "        satmin = 0.0,\n" +
            "        satmax = 1.0,\n" +
            "        valmin = 0.4,\n" +
            "        valmax = 1.0,\n" +
            "        last = false,\n" +
            "        thresh = 150,\n" +
            "        down = false,\n" +
            "        wasdown = false,\n" +
            "        movethresh = 4,\n" +
            "        brightthresh = 300,\n" +
            "        overthresh = 1000,\n" +
            "        avg = 0,\n" +
            "        state = 0;\n" +
            "    // States:\n" +
            "    // 0 waiting for gesture\n" +
            "    // 1 waiting for next move after gesture\n" +
            "    // 2 waiting for gesture to end\n" +
            "\n" +
            "    navigator.webkitGetUserMedia({\n" +
            "        audio: false,\n" +
            "        video: true\n" +
            "    }, function (stream) {\n" +
            "        video.src = window.URL.createObjectURL(stream);\n" +
            "        video.addEventListener('play',\n" +
            "            function () {\n" +
            "                setInterval(dump, 1000 / 25);\n" +
            "            });\n" +
            "    }, function () {\n" +
            "        throw new Error('OOOOOOOH! DEEEEENIED!');\n" +
            "    });\n" +
            "\n" +
            "    function dump() {\n" +
            "        if (canvas.width != video.videoWidth) {\n" +
            "            width = Math.floor(video.videoWidth / compression);\n" +
            "            height = Math.floor(video.videoHeight / compression);\n" +
            "            canvas.width = width;\n" +
            "            canvas.height = height;\n" +
            "            // ccanvas.width = width;\n" +
            "            // ccanvas.height = height;\n" +
            "        }\n" +
            "        _.drawImage(video, width, 0, -width, height);\n" +
            "        draw = _.getImageData(0, 0, width, height);\n" +
            "        //c_.putImageData(draw,0,0);\n" +
            "        skinfilter();\n" +
            "        test();\n" +
            "    }\n" +
            "\n" +
            "    function skinfilter() {\n" +
            "        var skin_filter = _.getImageData(0, 0, width, height),\n" +
            "            total_pixels = skin_filter.width * skin_filter.height,\n" +
            "            index_value = total_pixels * 4,\n" +
            "            count_data_big_array = 0;\n" +
            "        for (var y = 0; y < height; y++) {\n" +
            "            for (var x = 0; x < width; x++) {\n" +
            "                index_value = x + y * width;\n" +
            "                var r = draw.data[count_data_big_array],\n" +
            "                    g = draw.data[count_data_big_array + 1],\n" +
            "                    b = draw.data[count_data_big_array + 2],\n" +
            "                    a = draw.data[count_data_big_array + 3];\n" +
            "\n" +
            "                var hsv = rgb2Hsv(r, g, b);\n" +
            "                //When the hand is too lose (hsv[0] > 0.59 && hsv[0] < 1.0)\n" +
            "                //Skin Range on HSV values\n" +
            "                if (((hsv[0] > huemin && hsv[0] < huemax) || (hsv[0] > 0.59 && hsv[0] < 1.0)) && (hsv[1] > satmin && hsv[1] < satmax) && (hsv[2] > valmin && hsv[2] < valmax)) {\n" +
            "                    skin_filter[count_data_big_array] = r;\n" +
            "                    skin_filter[count_data_big_array + 1] = g;\n" +
            "                    skin_filter[count_data_big_array + 2] = b;\n" +
            "                    skin_filter[count_data_big_array + 3] = a;\n" +
            "                } else {\n" +
            "                    skin_filter.data[count_data_big_array] =\n" +
            "                        skin_filter.data[count_data_big_array + 1] =\n" +
            "                        skin_filter.data[count_data_big_array + 2] = 0;\n" +
            "                    skin_filter.data[count_data_big_array + 3] = 0;\n" +
            "                }\n" +
            "\n" +
            "                count_data_big_array = index_value * 4;\n" +
            "            }\n" +
            "        }\n" +
            "        draw = skin_filter;\n" +
            "    }\n" +
            "\n" +
            "    function rgb2Hsv(r, g, b) {\n" +
            "        r = r / 255;\n" +
            "        g = g / 255;\n" +
            "        b = b / 255;\n" +
            "        var max = Math.max(r, g, b),\n" +
            "            min = Math.min(r, g, b),\n" +
            "            h, s, v = max,\n" +
            "            d = max - min;\n" +
            "        s = max === 0 ? 0 : d / max;\n" +
            "\n" +
            "        if (max == min) {\n" +
            "            h = 0; // achromatic\n" +
            "        } else {\n" +
            "\n" +
            "            switch (max) {\n" +
            "            case r:\n" +
            "                h = (g - b) / d + (g < b ? 6 : 0);\n" +
            "                break;\n" +
            "            case g:\n" +
            "                h = (b - r) / d + 2;\n" +
            "                break;\n" +
            "            case b:\n" +
            "                h = (r - g) / d + 4;\n" +
            "                break;\n" +
            "            }\n" +
            "            h /= 6;\n" +
            "        }\n" +
            "\n" +
            "        return [h, s, v];\n" +
            "    }\n" +
            "\n" +
            "    function test() {\n" +
            "        var delt = _.createImageData(width, height),\n" +
            "            totalx, totaly, totald, totaln, dscl, pix;\n" +
            "        if (last !== false) {\n" +
            "            totalx = 0;\n" +
            "            totaly = 0;\n" +
            "            totald = 0;\n" +
            "            totaln = delt.width * delt.height;\n" +
            "            dscl = 0;\n" +
            "            pix = totaln * 4;\n" +
            "            while (pix -= 4) {\n" +
            "                var d = Math.abs(draw.data[pix] - last.data[pix]) + Math.abs(draw.data[pix + 1] - last.data[pix + 1]) + Math.abs(draw.data[pix + 2] - last.data[pix + 2]);\n" +
            "                if (d > thresh) {\n" +
            "                    delt.data[pix] = 160;\n" +
            "                    delt.data[pix + 1] = 255;\n" +
            "                    delt.data[pix + 2] =\n" +
            "                        delt.data[pix + 3] = 255;\n" +
            "                    totald += 1;\n" +
            "                    totalx += ((pix / 4) % width);\n" +
            "                    totaly += (Math.floor((pix / 4) / delt.height));\n" +
            "                } else {\n" +
            "                    delt.data[pix] =\n" +
            "                        delt.data[pix + 1] =\n" +
            "                        delt.data[pix + 2] = 0;\n" +
            "                    delt.data[pix + 3] = 0;\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        //slide.setAttribute('style','display:initial')\n" +
            "        //slide.value=(totalx/totald)/width\n" +
            "        if (totald) {\n" +
            "            down = {\n" +
            "                x: totalx / totald,\n" +
            "                y: totaly / totald,\n" +
            "                d: totald\n" +
            "            };\n" +
            "            handledown();\n" +
            "        }\n" +
            "        //console.log(totald)\n" +
            "        last = draw;\n" +
            "        //c_.putImageData(delt, 0, 0);\n" +
            "    }\n" +
            "\n" +
            "    function calibrate() {\n" +
            "        wasdown = {\n" +
            "            x: down.x,\n" +
            "            y: down.y,\n" +
            "            d: down.d\n" +
            "        };\n" +
            "    }\n" +
            "\n" +
            "    function handledown() {\n" +
            "        avg = 0.9 * avg + 0.1 * down.d;\n" +
            "        var davg = down.d - avg,\n" +
            "            good = davg > brightthresh;\n" +
            "        //console.log(davg)\n" +
            "        switch (state) {\n" +
            "        case 0:\n" +
            "            if (good) { //Found a gesture, waiting for next move\n" +
            "                console.log(\"Waiting for gesture\");\n" +
            "                state = 1;\n" +
            "                calibrate();\n" +
            "            }\n" +
            "            break;\n" +
            "        case 2: //Wait for gesture to end\n" +
            "            if (!good) { //Gesture ended\n" +
            "                console.log(\"Gesture ended.\");\n" +
            "                console.log(\"--------------------------------\");\n" +
            "                state = 0;\n" +
            "            }\n" +
            "            break;\n" +
            "        case 1: //Got next move, do something based on direction\n" +
            "            var dx = down.x - wasdown.x,\n" +
            "                dy = down.y - wasdown.y;\n" +
            "            var isHorizontalMovement = Math.abs(dy) < Math.abs(dx); //(dx,dy) is on a bowtie\n" +
            "            console.log(\"dx: \" + dx);\n" +
            "            console.log(\"isHorizontalMovement: \" + isHorizontalMovement);\n" +
            "            //console.log(good,davg)\n" +
            "            if (dx < -movethresh && isHorizontalMovement) {\n" +
            "                console.log('right');\n" +
            "                Reveal.navigateRight();\n" +
            "            } else if (dx > movethresh && isHorizontalMovement) {\n" +
            "                console.log('left');\n" +
            "                Reveal.navigateLeft();\n" +
            "            }\n" +
            "            if (dy > movethresh && !isHorizontalMovement) {\n" +
            "                if (davg > overthresh) {\n" +
            "                    console.log('over up');\n" +
            "                    Reveal.toggleOverview();\n" +
            "                } else {\n" +
            "                    console.log('up');\n" +
            "                    Reveal.navigateUp();\n" +
            "                }\n" +
            "            } else if (dy < -movethresh && !isHorizontalMovement) {\n" +
            "                if (davg > overthresh) {\n" +
            "                    console.log('over down');\n" +
            "                    Reveal.toggleOverview();\n" +
            "                } else {\n" +
            "                    console.log('down');\n" +
            "                    Reveal.navigateDown();\n" +
            "                }\n" +
            "            }\n" +
            "            state = 2;\n" +
            "            break;\n" +
            "        }\n" +
            "    }\n" +
            "}\n";

    private static  String TAG = "AGC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDecorView = getWindow().getDecorView();

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
}
