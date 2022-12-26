package smartMiniPrinter;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Iterator;

import android.graphics.Typeface;

import android.os.Build;

import android.widget.Toast;

import java.util.concurrent.TimeUnit;
import java.lang.reflect.Method;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;

import android.content.Intent;
import android.content.IntentFilter;

import android.os.SystemClock;

import android.Manifest;
import android.annotation.TargetApi;

import android.content.pm.PackageManager;
import android.content.res.Configuration;

import android.text.TextUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dm.commonlib.ThreadPoolManager;
import com.dm.commonlib.Utils;
import com.dm.minilib.DMPrinterManager;
import com.prints.printerservice.IPrinterCallback;

import java.util.HashMap;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

/**
 * This class echoes a string called from JavaScript.
 */
public class SmartMiniPrinter extends CordovaPlugin {
    private static final String TAG = "MINI";

    CallbackContext callbackContext = null;
    Context context = null;
    private Handler progressHandler = new Handler();

    private boolean openHead;
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        this.callbackContext = callbackContext;
        context = cordova.getActivity().getApplicationContext();
        DMPrinterManager dmPrinterManager = new DMPrinterManager(cordova.getActivity());
        IPrinterCallback.Stub mCallback = new IPrinterCallback.Stub() {
            @Override
            public void onException(int i, String s) {
                Log.e("======print exception " + " " + s, "onException");
                callbackContext.error(-1);

            }

            @Override
            public void onLength(long l, long l1) {
                Log.e("======print", "onLength: current = " + l + " total = " + l1);
            }

            @Override
            public void onStart() {
                Log.e("======print", "onStart");
            }

            @Override
            public void onComplete() {
                Log.e("======print", "onComplete");
                callbackContext.success(0);
            }
        };

        if (action.equals("printText")) {
            final String text = args.getString(0);
            Map < String, Integer > map = new HashMap < > ();
            map.put(Utils.KEY_ALIGN, 0);
            map.put(Utils.KEY_TYPEFACE, 0);
            map.put(Utils.KEY_TEXTSIZE, 26);

            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        dmPrinterManager.init(cordova.getActivity());
                        dmPrinterManager.printTextApi(text, map, mCallback);
                        dmPrinterManager.wrapPaperApi(1, null);
                        callbackContext.success(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.toString());
                        callbackContext.error(-1);
                    }
                }
            });

            return true;
        } else if (action.equals("printBitmap")) {
            final String text = args.getString(0);
            byte[] decodedString = Base64.decode(text, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            Map < String, Integer > map = new HashMap < > ();
            map.put(Utils.KEY_ALIGN, 0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {

                    try {
                        dmPrinterManager.init(cordova.getActivity());
                        dmPrinterManager.printBitmapApi(decodedByte, map, mCallback);
                        dmPrinterManager.wrapPaperApi(1, null);
                        callbackContext.success(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.toString());
                        callbackContext.error(-1);

                    }
                }
            });

            return true;
        } else if (action.equals("init")) {

            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        dmPrinterManager.init(cordova.getActivity());
                        callbackContext.success(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.toString());
                        callbackContext.error(-1);
                    }
                }
            });
            return true;

        } else if (action.equals("printBarCode")) {
            final String text = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        dmPrinterManager.init(cordova.getActivity());
                        dmPrinterManager.printBarCodeApi(text, 0, 360, 100, true, mCallback);
                        dmPrinterManager.wrapPaperApi(1, null);
                        callbackContext.success(0);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.toString());
                        callbackContext.error(-1);
                    }
                }
            });
            return true;

        } else if (action.equals("printQrcode")) {
            final String text = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        dmPrinterManager.init(cordova.getActivity());
                        dmPrinterManager.printQrcodeApi(text, 0, 4, mCallback);
                        dmPrinterManager.wrapPaperApi(1, null);
                        callbackContext.success(0);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.toString());
                        callbackContext.error(-1);
                    }
                }
            });
            return true;
        } else if (action.equals("feedPaper")) {
            final String text = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        dmPrinterManager.init(cordova.getActivity());
                        dmPrinterManager.wrapPaperApi(Integer.parseInt(text), null);
                        callbackContext.success(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.toString());
                        callbackContext.error(-1);
                    }
                }
            });
            return true;
        }


        return false;
    }

}
