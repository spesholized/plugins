package io.flutter.plugins.fileselector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.Nullable;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener;
import java.util.ArrayList;

class FileSelector implements ActivityResultListener {

  private final Context applicationContext;

  @Nullable private Activity activity;

  private MethodChannel.Result pendingResult;

  FileSelector(Context applicationContext, @Nullable Activity activity) {
    this.applicationContext = applicationContext;
    this.activity = activity;
  }

  void setActivity(@Nullable Activity activity) {
    this.activity = activity;
  }

  public void openFileSelector(
      @Nullable final String[] extensions,
      @Nullable final String[] mimeTypes,
      final boolean allowMultiSelection,
      final MethodChannel.Result result) {
    if (this.pendingResult != null) {
      result.error(
          "alreadyActiveError",
          "Cannot open file selector when there is already one active.",
          null);
      return;
    }
    pendingResult = result;

    final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    intent.setType("*/*");
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
    intent.addCategory(Intent.CATEGORY_OPENABLE);

    activity.startActivityForResult(intent, 123);
  }

  @Override
  public boolean onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    Log.v("#### request_result ", String.valueOf(requestCode) + " " + String.valueOf(resultCode));
    Log.v("#### data: ", data.getData().toString());
    if (requestCode != 123) {
      return false;
    }
    if (pendingResult != null) {
      if (data == null) {
        pendingResult.success(null);
      } else {
        ArrayList<String> result = new ArrayList<>();
        result.add(data.getData().toString());
        pendingResult.success(result);
      }
      pendingResult = null;
    }
    return true;
  }
}
