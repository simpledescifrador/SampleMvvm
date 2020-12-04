package com.jimac.vetclinicapp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {

    private static final Pattern IP_ADDRESS
            = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");


    private static ProgressDialog dialog;

    private static Pattern pattern;

    private static Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String TAG = "AppUtils";

    @SuppressLint("SimpleDateFormat")
    public static String convertTime(String time) {
        String convertedTime = time;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time);
            convertedTime = new SimpleDateFormat("hh:mm aa").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return convertedTime;
    }

    public static String getCurrentDateTime(String format) {
        return getTimeByFormat(format);
    }

    public static String getCurrentDateTime() {
        return getTimeByFormat("dd-MMM-yyyy hh:mm:ss");
    }

    public static String getCurrentTime(String format) {
        return getTimeByFormat(format);
    }

    public static String getCurrentTime() {
        return getTimeByFormat("hh:mm");
    }

    public static long getCurrentTimeInMillis() {
        long currentTimeMillis;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        currentTimeMillis = calendar.getTimeInMillis();
        return currentTimeMillis;
    }

    public static Date getDateFromString(String date, String format) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(date);
    }

    public static String getFormatFromDecimalValue(Double value) {
        String amount = String.valueOf(value);
        amount = amount.replaceAll(",", "");
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(Double.valueOf(amount));
    }

    public static String getFormatFromIntegerValue(int value) {
        String amount = String.valueOf(value);
        amount = amount.replaceAll(",", "");
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(Double.valueOf(amount));

    }

    public static long getHoursDifferent(long startTime, long currentTime) {
        long hours = 0;
        long diff = currentTime - startTime;
        hours = diff / (60 * 60 * 1000);
        return hours;
    }

    public static String getLocalDateFromUTCTime(String utcTime) {

        String dateString = "";

        try {

            String time = getLocalFromUTC(utcTime);

            String[] timePice = time.split("T");
            String tempDate = timePice[0];
            String tempTime = timePice[1].substring(0, 8);

            String[] date = tempDate.split("-");

            String year = date[0];
            String month = date[1];
            String day = date[2];

            dateString = day + "/" + month + "/" + year;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dateString;
    }

    public static String getLocalFromUTC(String utcTime) {
        /**  2017-10-22T18:22:37.000+06:00  **/

        try {
            String newUtcTime = utcTime.substring(0, utcTime.indexOf("+"));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date = dateFormat.parse(newUtcTime);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            return simpleDateFormat.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                /** "created_at": "2017-08-15T10:29:20.000Z"  &&  **/
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                dateFormat.setTimeZone(TimeZone.getTimeZone("BST"));
                Date date = dateFormat.parse(utcTime);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                return simpleDateFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static String getLocalTimeFromUTCTime(String utcTime) {
        String tempTime = "";
        try {
            String time = getLocalFromUTC(utcTime);
            String[] timePice = time.split("T");
            tempTime = timePice[1].substring(0, 8);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tempTime;
    }

    public static String getMonthName(String value) {

        if (value.equals("01") || value.equals("1")) {
            return "Jan";
        }
        if (value.equals("02") || value.equals("2")) {
            return "Feb";
        }
        if (value.equals("03") || value.equals("3")) {
            return "Mar";
        }
        if (value.equals("04") || value.equals("4")) {
            return "Apr";
        }
        if (value.equals("05") || value.equals("5")) {
            return "May";
        }
        if (value.equals("06") || value.equals("6")) {
            return "Jun";
        }
        if (value.equals("07") || value.equals("7")) {
            return "Jul";
        }
        if (value.equals("08") || value.equals("8")) {
            return "Aug";
        }
        if (value.equals("09") || value.equals("9")) {
            return "Sep";
        }
        if (value.equals("10") || value.equals("10")) {
            return "Oct";
        }
        if (value.equals("11")) {
            return "Nov";
        }
        if (value.equals("12")) {
            return "Dec";
        }
        return "Not Found";
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(((View) view).getWindowToken(), 0);
    }

    public static boolean isGPSEnable(Context context) {

        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        }
        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isValidEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidIP(String ip) {
        boolean validate = false;
        Matcher matcher = IP_ADDRESS.matcher(ip);
        if (matcher.matches()) {
            return true;
        }
        return validate;
    }

    public static boolean isValidURL(String urlStr) {
        return Patterns.WEB_URL.matcher(urlStr).matches();
    }

    public static void log(String TAG, String message) {
        Log.d(TAG, message);
    }

    public static void showDialog(Context context, String title, String message) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public static void showGPSEnableDialog(final Context context) {
        showGPSEnableDialog(context, "Your GPS seems to be disabled, do you want to enable it?", "Yes", "No");
    }

    public static void showGPSEnableDialog(final Context context, String message, String positiveButtonText,
            String negativeButtonText) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                            @SuppressWarnings("unused") final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showMessageDialog(Context context, String title, String message, int imageId) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(imageId)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    private static String getTimeByFormat(String format) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getID()));
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

}
