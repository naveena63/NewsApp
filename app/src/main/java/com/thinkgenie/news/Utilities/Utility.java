package com.thinkgenie.news.Utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.thinkgenie.news.R;


public class Utility {

    private static ProgressDialog progressDialog;

    public static boolean isNetworkAvailable(Activity activity) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isConnected()) {
                return false;
                /* aka, do nothing */
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean isValidEmail(CharSequence paramCharSequence) {
        if (paramCharSequence == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(paramCharSequence).matches();
    }


    public static void showProgress(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    public static void hideProgress(Activity activity) {
        progressDialog.dismiss();
    }


    public static void hideKeyboard(Activity ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((AppCompatActivity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void showToast(Context c, String s, boolean duration) {
        if (c == null) return;
        Toast tst = Toast.makeText(c, s, duration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        tst.show();
    }

    public static void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusableInTouchMode(false);
    }

    public static void enableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setEnabled(true);
        editText.setCursorVisible(true);
        editText.setFocusableInTouchMode(true);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        editText.requestFocus();

    }


//    public static void updateAndroidSecurityProvider(Activity callingActivity) {
//        try {
//            ProviderInstaller.installIfNeeded(callingActivity);
//        } catch (GooglePlayServicesRepairableException e) {
//            // Thrown when Google Play Services is not installed, up-to-date, or enabled
//            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
//            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
//        } catch (GooglePlayServicesNotAvailableException e) {
//            Log.e("SecurityException", "Google Play Services not available.");
//        }
//    }

    public static void showSnackBar(Context activity, CoordinatorLayout coordinatorLayout, String text, int type) {
        if (coordinatorLayout == null || text == null || activity == null) {
            return;
        }
        Snackbar snackBar = Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_SHORT);
        TextView txtMessage = (TextView) snackBar.getView().findViewById(R.id.snackbar_text);
        txtMessage.setTextColor(ContextCompat.getColor(activity, R.color.white));
        if (type == 2)
            snackBar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.black));
        else if (type == 1)
            snackBar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.app_snack_bar_true));
        else {
            snackBar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.mainColorPrimary));
        }
        txtMessage.setTypeface(getTypeface(1, activity));

        snackBar.show();
    }



    public static Typeface getTypeface(int value, Context context) {
        String path = "fonts/opensans/OpenSans-Regular.ttf";

        switch (value) {
            // open sans
            case 0:
                path = "fonts/opensans/OpenSans-Light.ttf";
                break;
            case 1:
                path = "fonts/opensans/OpenSans-Regular.ttf";
                break;
            case 2:
                path = "fonts/opensans/OpenSans-SemiBold.ttf";

                break;
            case 3:
                path = "fonts/opensans/OpenSans-Bold.ttf";
                break;

            // roboto
            case 4:
                path = "fonts/roboto/Roboto-Light.ttf";
                break;
            case 5:
                path = "fonts/roboto/Roboto-Regular.ttf";
                break;
            case 6:
                path = "fonts/roboto/Roboto-Medium.ttf";
                break;
            case 7:
                path = "fonts/roboto/Roboto-Bold.ttf";
                break;


        }
        return Typeface.createFromAsset(context.getAssets(), path);
    }

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }


}
