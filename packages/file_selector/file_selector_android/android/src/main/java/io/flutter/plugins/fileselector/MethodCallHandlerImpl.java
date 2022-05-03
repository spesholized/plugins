package io.flutter.plugins.fileselector;

import android.util.Log;
import androidx.annotation.Nullable;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import java.util.ArrayList;
import java.util.HashMap;

final class MethodCallHandlerImpl implements MethodCallHandler {
  private static final String TAG = "MethodCallHandlerImpl";
  private final FileSelector fileSelector;
  @Nullable private MethodChannel channel;

  MethodCallHandlerImpl(FileSelector fileSelector) {
    this.fileSelector = fileSelector;
    System.out.println("##### MethodCallHandlerImpl");
    Log.v(TAG, "###### MethodCallHandlerImpl");
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    Log.v(TAG, "##### " + call.method);
    // arguments is Hashmap
    // acceptedTypeGroups is ArrayList
    ArrayList<HashMap<String, Object>> typeGroups = call.argument("acceptedTypeGroups");
    ArrayList<String> mimeTypes = (ArrayList<String>) typeGroups.get(0).get("mimeTypes");
    Log.v("#### typeGroups.get(0)", typeGroups.get(0).toString());
    Log.v("#### get class", typeGroups.get(0).get("extensions").getClass().toString());
    // Log.v("####", typeGroups.get(0).get("extensions"));
    // Log.v("####", typeGroups.get(0).get("mimeTypes"));
    // Log.v("####", typeGroups.get(0).get("macUTIs"));

    fileSelector.openFileSelector(new String[0], mimeTypes.toArray(new String[0]), false, result);
  }

  void startListening(BinaryMessenger messenger) {
    if (channel != null) {
      Log.wtf(TAG, "Setting a method call handler before the last was disposed.");
      stopListening();
    }

    channel = new MethodChannel(messenger, "plugins.flutter.io/file_selector_android");
    channel.setMethodCallHandler(this);
  }

  void stopListening() {
    if (channel == null) {
      Log.d(TAG, "Tried to stop listening when no MethodChannel had been initialized.");
      return;
    }

    channel.setMethodCallHandler(null);
    channel = null;
  }
}
