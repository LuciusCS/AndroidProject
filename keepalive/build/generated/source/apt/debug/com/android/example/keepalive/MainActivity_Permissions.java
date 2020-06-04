// Generated code from Permission. Do not modify!
package com.android.example.keepalive;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.cmonbaby.permission.core.listener.PermissionSetting;
import com.cmonbaby.permission.core.listener.ProceedPermission;
import com.cmonbaby.permission.core.listener.RequestPermission;
import com.cmonbaby.permission.core.utils.PermissionUtils;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;

/**
 * <p>Author:		Simon
 * <p>QQ:			8950764
 * <p>Email:		simon@cmonbaby.com
 * <p>WebSize:		https://www.cmonbaby.com
 * <p>Version:		1.1.0
 * <p>Date:			2019/11/24
 * <p>Description:	Generated code
 */
public final class MainActivity_Permissions implements RequestPermission<MainActivity> {
  private static final int REQUEST_CODE = 666;

  private static String[] PERMISSIONS;

  @Override
  public void requestPermissions(@NonNull MainActivity target, String[] permissions) {
    PERMISSIONS = permissions;
    if (PermissionUtils.hasSelfPermissions(target, PERMISSIONS)) {
      target.permissionGranted();
    } else if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSIONS)) {
      target.showRationale(new ProceedPermissionImpl(target));
    } else {
      ActivityCompat.requestPermissions(target, PERMISSIONS, REQUEST_CODE);
    }
  }

  @Override
  public void onRequestPermissionsResult(@NonNull MainActivity target, int requestCode,
      @NonNull int[] grantResults) {
    if (requestCode != REQUEST_CODE) return;
    if (PermissionUtils.verifyPermissions(grantResults)) {
      target.permissionGranted();
    } else if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSIONS)) {
      target.neverAskAgain(new PermissionSettingImpl(target));
    } else {
      target.permissionDenied();
    }
  }

  private static final class ProceedPermissionImpl implements ProceedPermission {
    private final WeakReference<MainActivity> weakTarget;

    private ProceedPermissionImpl(@NonNull MainActivity target) {
      this.weakTarget = new WeakReference(target);
    }

    @Override
    public void proceed() {
      MainActivity target = this.weakTarget.get();
      if (target == null) return;
      ActivityCompat.requestPermissions(target, PERMISSIONS, REQUEST_CODE);
    }
  }

  private static final class PermissionSettingImpl implements PermissionSetting {
    private final WeakReference<MainActivity> weakTarget;

    private PermissionSettingImpl(@NonNull MainActivity target) {
      this.weakTarget = new WeakReference(target);
    }

    @Override
    public void setting(int settingCode) {
      MainActivity target = this.weakTarget.get();
      if (target == null) return;
      Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
      Uri uri = Uri.fromParts("package", target.getPackageName(), null);
      intent.setData(uri);
      target.startActivityForResult(intent, settingCode);
    }
  }
}
