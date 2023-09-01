package com.reactlibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ComponentActivity;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.modules.core.DeviceEventManagerModule;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import webinstats.android_wis.Webinstats;

public class ReactWisModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private Webinstats webinstats = null;
    public ReactWisModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "ReactWis";
    }

    /*@ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {

        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }*/
    @ReactMethod
    public void execute(ReadableMap readableMap/*, Promise onSuccess*/){
        Map<String,String> nativemap = new HashMap<>();
        String s="",_cburl="";
        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
        while(iterator.hasNextKey()){
            String key = iterator.nextKey();
            ReadableType type = readableMap.getType(key);
            switch (type) {
                case Null:
                    nativemap.put(key, null);
                    break;
                case Boolean:
                    nativemap.put(key, String.valueOf(readableMap.getBoolean(key)));
                    break;
                case Number:
                    nativemap.put(key, String.valueOf(readableMap.getDouble(key)));
                    break;
                case String:
                    nativemap.put(key, readableMap.getString(key));
                    if (key.equals("s")){
                        s = readableMap.getString(key);
                    }
                    if (key.equals("_cburl")){
                        _cburl = readableMap.getString(key);
                    }
                    break;
            }
        }
        if (s.equals("")){
            return;
        }
        if (_cburl.equals("")){
            _cburl = "//wis.webinstats.com/";
        }
        nativemap.put("_enable_push","1");
        nativemap.put("_wis_plt","React-Native");
        //new Webinstats(_cburl,s,"0").sendPageview(getCurrentActivity(),nativemap);
        if(webinstats == null){
            webinstats = new Webinstats(_cburl,s,"0");
        }
        webinstats.execute(getCurrentActivity(),nativemap);
        ComponentActivity activity =  new ComponentActivity();
        //onSuccess.resolve("token based");

    }
    @ReactMethod
    public void setInlineCallback(String companyId,final Callback inlineCallback){
        if (companyId.equals("") || companyId.equals("0")){
                Log.i("webinstats_log","company id null");
                return;
        }
        if(webinstats == null){
           webinstats = new Webinstats("//wis.webinstats.com/",companyId,"0");
        }
        webinstats.setInlineCallback(new Webinstats.InlineCallback() {
            @Override
            public void onSuccess(String html,String object) {
                Log.v("webinstats_log","inline callback triggered ");
                inlineCallback.invoke(html,object);
            }

            @Override
            public void onError() {
            }
        });

    }
    @ReactMethod
    public void saveTestParameters(String companyId,String url){
        try{
            String _cburl="";
            if (companyId.equals("")){
                return;
            }
            if (_cburl.equals("")){
                _cburl = "//wis.webinstats.com/";
            }
            if(webinstats == null){
                webinstats = new Webinstats(_cburl,companyId,"0");
            }
            webinstats.saveTestParameters(getCurrentActivity(), Uri.parse(url),null);
            Log.v("webinstats_log","saveTestParams called");
        } catch (Exception e) {
            Log.v("webinstats_log","saveTestParams error :"+e.getMessage());
        }

    }

    private void sendEvent(final ReactContext mReactContext,
                           String eventName,
                           WritableMap params) {
        mReactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }


    @ReactMethod
    public void setPushClickCallback(String companyId, final Callback pushClick){
        Log.i("webinstats_log","setPushClickCallback called");
        if (companyId.equals("") || companyId.equals("0")){
            Log.i("webinstats_log","company id null");
            return;
        }
        reactContext.hasActiveCatalystInstance();
        SharedPreferences wis_cookie = getReactApplicationContext().getSharedPreferences("wis_defaults", Context.MODE_PRIVATE);
        if(wis_cookie.getString("_react_wisLaunchUrl",null) != null){
            String launchUrl = wis_cookie.getString("_react_wisLaunchUrl","");
            wis_cookie.edit().remove("_react_wisLaunchUrl").apply();
            WritableMap remoteMessage = Arguments.createMap();
            remoteMessage.putString("l",launchUrl);
            sendEvent( getReactApplicationContext(),"pushClicked", remoteMessage);
        }
    }

    @ReactMethod
    public void addItem(String companyId,String domain,String productId, String quantity, String price, String category, String title){
        try{
            if (domain.equals("")){
                domain = "//wis.webinstats.com/";
            }
            if (companyId.equals("") || companyId.equals("0")){
                Log.i("webinstats_log","company id null");
                return;
            }
            if(webinstats == null){
                webinstats = new Webinstats(domain,companyId,"0");
            }
            webinstats.addItem(productId,quantity,price,category,title);
        }catch (Exception ex){
            ex.getMessage();
        }
    }

}
