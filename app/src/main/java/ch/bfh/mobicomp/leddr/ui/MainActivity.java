package ch.bfh.mobicomp.leddr.ui;

import android.accounts.OperationCanceledException;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.github.kevinsawicki.wishlist.Toaster;
import com.squareup.otto.Subscribe;

import x.inject.Inject;

import butterknife.ButterKnife;
import ch.bfh.mobicomp.leddr.BootstrapServiceProvider;
import ch.bfh.mobicomp.leddr.R;
import ch.bfh.mobicomp.leddr.core.BootstrapService;
import ch.bfh.mobicomp.leddr.db.LeddrContract;
import ch.bfh.mobicomp.leddr.db.LeddrDbHelper;
import ch.bfh.mobicomp.leddr.events.NavItemSelectedEvent;
import ch.bfh.mobicomp.leddr.util.AppStartChecker;
import ch.bfh.mobicomp.leddr.util.Ln;
import ch.bfh.mobicomp.leddr.util.SafeAsyncTask;
import ch.bfh.mobicomp.leddr.util.UIUtils;


/**
 * Initial activity for the application.
 *
 * If you need to remove the authentication from the application please see
 * {@link ch.bfh.mobicomp.leddr.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class MainActivity extends BootstrapFragmentActivity {

    private static final String TAG = AppStartChecker.class.getSimpleName();

    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected AppStartChecker appStartChecker;

    private boolean userHasAuthenticated = false;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;

    private NavigationDrawerFragment navigationDrawerFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

//        this.deleteDatabase(LeddrDbHelper.DATABASE_NAME);

        switch (appStartChecker.checkAppStart(this, PreferenceManager.getDefaultSharedPreferences(this))) {
            case NORMAL:
                Ln.d("%s %s", TAG, "NORMAL");
                break;
            case FIRST_TIME_VERSION:
                Ln.d("%s %s", TAG, "FIRST_TIME_VERSION");
                break;
            case FIRST_TIME:
                Ln.d("%s %s", TAG, "FIRST_TIME");

                LeddrDbHelper leddrDbHelper = new LeddrDbHelper(this);

                // Gets the data repository in write mode
                SQLiteDatabase db = leddrDbHelper.getWritableDatabase();

                for (int i = 0; i < 10; i++) {
                    int id = i;
                    String name = String.format("%s %s", "LED Brick", i);
                    String uid = String.format("%s%s", "$5G2Fdfh66hsdfDFB#(54", i);

                    // Create a new map of values, where column names are the keys
                    ContentValues values = new ContentValues();
                    values.put(LeddrContract.DeviceEntry.COLUMN_NAME_NAME, name);
                    values.put(LeddrContract.DeviceEntry.COLUMN_NAME_DEVICE_ID, id);
                    values.put(LeddrContract.DeviceEntry.COLUMN_NAME_USER_ID, uid);

                    // Insert the new row, returning the primary key value of the new row
                    long newRowId;
                    newRowId = db.insert(
                            LeddrContract.DeviceEntry.TABLE_NAME,
                            null,
                            values);
                }

                break;
            default:
                Ln.d("%s %s", TAG, "DEFAULT");
                break;
        }

        if(isTablet()) {
            setContentView(R.layout.main_activity_tablet);
        } else {
            setContentView(R.layout.main_activity);
        }



        String deviceName = "no device selected";
        String deviceId = "no device selected";

        setTitle(deviceName);

        // View injection with Butterknife
        ButterKnife.inject(this);

        // Set up navigation drawer
        title = drawerTitle = getTitle();

        if(!isTablet()) {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerToggle = new ActionBarDrawerToggle(
                    this,                    /* Host activity */
                    drawerLayout,           /* DrawerLayout object */
//                    R.drawable.ic_drawer,    /* nav drawer icon to replace 'Up' caret */
                    R.string.navigation_drawer_open,    /* "open drawer" description */
                    R.string.navigation_drawer_close) { /* "close drawer" description */

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    getActionBar().setTitle(title);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    getActionBar().setTitle(drawerTitle);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };

            // Set the drawer toggle as the DrawerListener
            drawerLayout.setDrawerListener(drawerToggle);

            navigationDrawerFragment = (NavigationDrawerFragment)
                    getFragmentManager().findFragmentById(R.id.navigation_drawer);

            // Set up the drawer.
            navigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
        }


        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        checkAuth();

        Intent intent = getIntent();
        // Get the extras (if there are any)
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("DeviceName")) {
                deviceName = intent.getExtras().getString("DeviceName");
            }
            if (extras.containsKey("DeviceName")) {
                deviceId = intent.getExtras().getString("DeviceId");
            }
        } else {
            Toaster.showShort(MainActivity.this, "Please select a device");
            navigateToDevices();
        }


        setTitle(deviceName);

    }

    private boolean isTablet() {
        return UIUtils.isTablet(this);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(!isTablet()) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            drawerToggle.syncState();
        }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!isTablet()) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    private void initScreen() {
        if (userHasAuthenticated) {
            navigateToHome();
        }

    }

    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final BootstrapService svc = serviceProvider.getService(MainActivity.this);
                return svc != null;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
                    finish();
                }
            }

            @Override
            protected void onSuccess(final Boolean hasAuthenticated) throws Exception {
                super.onSuccess(hasAuthenticated);
                userHasAuthenticated = true;
                initScreen();
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (!isTablet() && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
//                menuDrawer.toggleMenu();
                return true;
            case R.id.timer:
                navigateToTimer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToHome() {
        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new CarouselFragment())
                .commit();
    }

    private void navigateToTimer() {
        final Intent i = new Intent(this, BootstrapTimerActivity.class);
        startActivity(i);
    }

    private void navigateToDevices() {

        // Create an instance of ExampleFragment
        DeviceListFragment deviceListFragment = new DeviceListFragment();

        // In case this activity was started with special instructions from an Intent,
        // pass the Intent's extras to the fragment as arguments
        deviceListFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'container' FrameLayout
        getFragmentManager().beginTransaction()
                .replace(R.id.container, deviceListFragment).commit();
    }

    private void navigateToAddDevice() {
        final Intent i = new Intent(this, AddDeviceActivity.class);
        startActivity(i);
    }

    private void navigateToAbout() {
       final Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }

    @Subscribe
    public void onNavigationItemSelected(NavItemSelectedEvent event) {

        Ln.d("Selected: %1$s", event.getItemPosition());

        switch(event.getItemPosition()) {
            case 0:
                // Home
                navigateToHome();
                break;
            case 1:
                // Timer
                navigateToTimer();
                break;
            case 2:
                // Devices
                navigateToDevices();
                break;
            case 3:
                navigateToAddDevice();
                break;
            case 4:
                // About
                navigateToAbout();
                break;
        }
    }
}
