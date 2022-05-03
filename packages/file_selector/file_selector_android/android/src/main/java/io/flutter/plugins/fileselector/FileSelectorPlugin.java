package io.flutter.plugins.fileselector;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;

public final class FileSelectorPlugin implements FlutterPlugin, ActivityAware {
  private static final String TAG = "FileSelectorPlugin";
  @Nullable private MethodCallHandlerImpl methodCallHandler;
  @Nullable private FileSelector fileSelector;

  /**
   * Registers a plugin implementation that uses the stable {@code io.flutter.plugin.common}
   * package.
   *
   * <p>Calling this automatically initializes the plugin. However plugins initialized this way
   * won't react to changes in activity or context, unlike {@link FileSelectorPlugin}.
   */
  @SuppressWarnings("deprecation")
  public static void registerWith(io.flutter.plugin.common.PluginRegistry.Registrar registrar) {
    Log.v("###### ", "resgistering");
    MethodCallHandlerImpl handler =
        new MethodCallHandlerImpl(new FileSelector(registrar.context(), registrar.activity()));
    handler.startListening(registrar.messenger());
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    fileSelector = new FileSelector(binding.getApplicationContext(), /*activity=*/ null);
    methodCallHandler = new MethodCallHandlerImpl(fileSelector);
    methodCallHandler.startListening(binding.getBinaryMessenger());
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    if (methodCallHandler == null) {
      Log.wtf(TAG, "Already detached from the engine.");
      return;
    }

    methodCallHandler.stopListening();
    methodCallHandler = null;
    fileSelector = null;
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    if (methodCallHandler == null) {
      Log.wtf(TAG, "fileSelector was never set.");
      return;
    }
    binding.addActivityResultListener(fileSelector);
    fileSelector.setActivity(binding.getActivity());
  }

  @Override
  public void onDetachedFromActivity() {
    if (methodCallHandler == null) {
      Log.wtf(TAG, "fileSelector was never set.");
      return;
    }

    fileSelector.setActivity(null);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity();
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    onAttachedToActivity(binding);
  }
}
