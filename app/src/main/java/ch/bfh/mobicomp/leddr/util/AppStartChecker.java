package ch.bfh.mobicomp.leddr.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import javax.inject.Inject;

import ch.bfh.mobicomp.leddr.core.Constants;

/**
 * Created by zimmerma on 02/05/15.
 */
public class AppStartChecker {
    /**
     * Distinguishes different kinds of app starts: <li>
     * <ul>
     * First start ever ({@link #FIRST_TIME})
     * </ul>
     * <ul>
     * First start in this version ({@link #FIRST_TIME_VERSION})
     * </ul>
     * <ul>
     * Normal app start ({@link #NORMAL})
     * </ul>
     *
     * @author zimmerma
     * inspired by
     * @author schnatterer
     *
     */

    private static final String TAG = AppStartChecker.class.getSimpleName();

    public enum AppStart {
        FIRST_TIME, FIRST_TIME_VERSION, NORMAL;
    }

    /**
     * The app version code (not the version name!) that was used on the last
     * start of the app.
     */
    private static final String LAST_APP_VERSION = "1";

    /**
     * Caches the result of {@link #checkAppStart(Context context, SharedPreferences sharedPreferences)}. To allow idempotent method
     * calls.
     */
    private static AppStart appStart = null;

    @Inject
    public AppStartChecker() {}

    /**
     * Finds out started for the first time (ever or in the current version).
     *
     * @return the type of app start
     */
    public AppStart checkAppStart(Context context, SharedPreferences sharedPreferences) {
        PackageInfo pInfo;

        try {
            pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            int lastVersionCode = sharedPreferences.getInt(
                    LAST_APP_VERSION, -1);
            // String versionName = pInfo.versionName;
            int currentVersionCode = pInfo.versionCode;
            appStart = checkAppStart(currentVersionCode, lastVersionCode);

            // Update version in preferences
            sharedPreferences.edit()
                    .putInt(LAST_APP_VERSION, currentVersionCode).commit(); // must use commit here or app may not update prefs in time and app will loop into walkthrough
        } catch (PackageManager.NameNotFoundException e) {
            Ln.w("%s %s", TAG, "Unable to determine current app version from package manager. Defensively assuming normal app start.");
        }
        return appStart;
    }

    public AppStart checkAppStart(int currentVersionCode, int lastVersionCode) {
        if (lastVersionCode == -1) {
            return AppStart.FIRST_TIME;
        } else if (lastVersionCode < currentVersionCode) {
            return AppStart.FIRST_TIME_VERSION;
        } else if (lastVersionCode > currentVersionCode) {
            Ln.w("%s %s", TAG, "Current version code (" + currentVersionCode
                    + ") is less then the one recognized on last startup ("
                    + lastVersionCode
                    + "). Defensively assuming normal app start.");
            return AppStart.NORMAL;
        } else {
            return AppStart.NORMAL;
        }
    }
}
