package ba.pohl1.hm.edu.vrlibrary.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;

/**
 * Utility class for UI related stuff.
 *
 * Created by Pohl on 03.04.2016.
 */
public final class UIUtils {

    private static final String DEFAULT_TITLE = "Information";

    private UIUtils() {
        // Prevents instantiation
    }

    public static void runInUIThread(final Runnable runnable) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }

    public static AlertDialog createDialog(final Context context, final String message) {
        return createBuilder(context, message).create();
    }

    public static AlertDialog createOkDialog(final Context context, final String message,
                                             final DialogInterface.OnClickListener onClickListener) {

        return createOkCancelBuilder(context, message, onClickListener, null).create();
    }

    public static AlertDialog.Builder createOkCancelBuilder(final Context context, final String message,
                                                            final DialogInterface.OnClickListener okListener,
                                                            final DialogInterface.OnClickListener cancelListener) {

        final AlertDialog.Builder builder = createBuilder(context, message);
        if(okListener != null) {
            builder.setPositiveButton("Ok", okListener);
        }
        if(cancelListener != null) {
            builder.setNegativeButton("Cancel", cancelListener);
        }
        return builder;
    }

    public static AlertDialog.Builder createBuilder(final Context context, final String message) {
        return createBuilder(context).setMessage(message);
    }

    public static AlertDialog.Builder createBuilder(final Context context) {
        return new AlertDialog.Builder(context).setTitle(DEFAULT_TITLE);
    }
}
