package frameworks.apppermission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import transport.school.com.schoolapp.R;
final public class PermissionsUtils {
    public static final String TAG = PermissionsUtils.class.getSimpleName();


    public static final String PERMISSION_CALL_PHONE =
            Manifest.permission.CALL_PHONE;


    public static boolean isPermissionAvailable(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, permission) !=
            PackageManager.PERMISSION_DENIED) {
            Log.d(TAG, permission + " is available");
            return true;
        }
        Log.d(TAG, permission + " is NOT available");
        return false;
    }


    public static void requestPermission(Activity activity, String permission, int requestCode) {

        Log.d(TAG, permission + " is denied, ask user");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{permission}, requestCode);
        }
    }


    public static boolean handlePermissionRequestResult(Activity activity, String[] permissions,
                                                        int[] grantResults) {
        boolean isGranted = true;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
        }

        Log.d(TAG, "handle permission request result : " + isGranted);
        if (!isGranted) {
            boolean shouldShowDeniedAlert = false;
            for (String permission : permissions) {
                shouldShowDeniedAlert = shouldShowDeniedAlert && !(ActivityCompat
                        .shouldShowRequestPermissionRationale(activity, permission));
                Log.d(TAG, "should alert : " + shouldShowDeniedAlert);
            }
            if (shouldShowDeniedAlert) {
                showPermissionAlert(activity);
            }
        }
        return isGranted;
    }

    public static void showPermissionAlert(final Context context) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setMessage(context.getString(R.string.permissions_denied_alert));
        mBuilder.setPositiveButton(context.getString(R.string.settings),
                                   new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           Intent permissionsIntent = new Intent(
                                                   "android.intent.action.MANAGE_APP_PERMISSIONS");
                                           permissionsIntent
                                                   .putExtra("android.intent.extra.PACKAGE_NAME",
                                                             context.getPackageName());
                                           permissionsIntent.putExtra("hideInfoButton", true);
                                           permissionsIntent
                                                   .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                           permissionsIntent
                                                   .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                           permissionsIntent.addFlags(
                                                   Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                           context.startActivity(permissionsIntent);
                                       }
                                   });
        mBuilder.setNegativeButton(context.getString(R.string.cancel),
                                   new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           //Do nothing...
                                       }
                                   });
        mBuilder.setCancelable(false);
        mBuilder.create().show();
    }
}
