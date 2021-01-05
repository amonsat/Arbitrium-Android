package com.example;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;
import android.os.Handler;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;
import android.content.Context;



public class MyOrangePlugin extends CordovaPlugin {
    private static final String TAG = "MyOrangePlugin";


    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
      super.initialize(cordova, webView);

      Log.d(TAG, "Initializing MyOrangePlugin");
    }

    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
      if (action.equals("poolexec")){
            final String cmd = args.getString(0);
            cordova.getThreadPool().execute(new Runnable() {
                    public void run() {
                            Process p;
                            StringBuffer output = new StringBuffer();
                            int exitStatus = 100;
                            try {
                                    p = Runtime.getRuntime().exec(cmd);
                                    BufferedReader reader = new BufferedReader( new InputStreamReader(p.getInputStream()) );
                                    String line = "";
                                    while ((line = reader.readLine()) != null) {
                                            output.append(line + "\n");
                                    }
                                    exitStatus = p.waitFor();
                            }
                            catch (IOException e) {
                                    e.printStackTrace();
                            } catch (InterruptedException e) {
                                    e.printStackTrace();
                            }

                            try {
                                    JSONObject json = new JSONObject();
                                    json.put("exitStatus", exitStatus);
                                    json.put("output", output.toString());
                                    callbackContext.success(json);
                            }
                            catch(JSONException e){
                                    e.printStackTrace();
                                    callbackContext.success();
                            }
                    }
            });
      }else if(action.equals("exec")) {
        final String cmd = args.getString(0);
        String exec_output = exec(cmd);
        final PluginResult result = new PluginResult(PluginResult.Status.OK, exec_output);
        callbackContext.sendPluginResult(result);
      } else if(action.equals("download")){
      String[] cmdArray;
      try {
                JSONArray cmdJsonArray = args.getJSONArray(0);
                cmdArray = new String[cmdJsonArray.length()];
                for (int i=0; i<cmdJsonArray.length(); ++i) {
                        cmdArray[i] = cmdJsonArray.getString(i);
                }

      } catch (JSONException e) {
                cmdArray = new String[]{ args.getString(0) };
      }
        Context context = this.cordova.getActivity().getApplicationContext();
        String storagePath = context.getFilesDir().getParentFile().getPath();
        if (cmdArray.length>2){
          storagePath = cmdArray[2];
        }
        String url = cmdArray[0];
        storagePath = storagePath + "/" + cmdArray[1];
        boolean down_status = download(url, storagePath);
        String pingPlugin = "";
        if (down_status==true){
          pingPlugin = storagePath;
        }
        final PluginResult result = new PluginResult(PluginResult.Status.OK, pingPlugin);
        callbackContext.sendPluginResult(result);
      }
      return true;
    }


    private String exec(String command) {
      String str_output = "false";
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            process.waitFor();
            str_output = output.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
      return str_output;
    }


    private boolean download(String urlStr, String localPath) {
      boolean down_stat = false;
      try {
      URL url = new URL(urlStr);
      HttpURLConnection urlconn = (HttpURLConnection)url.openConnection();
      urlconn.setRequestMethod("GET");
      urlconn.setInstanceFollowRedirects(true);
      urlconn.connect();
      InputStream in = urlconn.getInputStream();
      FileOutputStream out = new FileOutputStream(localPath);
      int read;
      byte[] buffer = new byte[4096];
      while ((read = in.read(buffer)) > 0) {
        out.write(buffer, 0, read);
      }
      out.close();
      in.close();
      urlconn.disconnect();
      down_stat = true;
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return down_stat;
    }


}
