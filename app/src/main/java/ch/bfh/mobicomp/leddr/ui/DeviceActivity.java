package ch.bfh.mobicomp.leddr.ui;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import ch.bfh.mobicomp.leddr.BootstrapServiceProvider;
import ch.bfh.mobicomp.leddr.R;
import ch.bfh.mobicomp.leddr.events.NavItemSelectedEvent;
import ch.bfh.mobicomp.leddr.util.AppStartChecker;
import ch.bfh.mobicomp.leddr.util.Ln;
import ch.bfh.mobicomp.leddr.util.UIUtils;


/**
 * Initial activity for the application.
 *
 * If you need to remove the authentication from the application please see
 * {@link ch.bfh.mobicomp.leddr.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class DeviceActivity extends BootstrapFragmentActivity {

    private static final String TAG = AppStartChecker.class.getSimpleName();

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private NavigationDrawerFragment navigationDrawerFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        if(isTablet()) {
            setContentView(R.layout.device_list_tablet);
        } else {
            setContentView(R.layout.device_list);
        }

        setTitle(R.string.title_device_list);

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
                    Ln.d("onDrawerClosed Title %s", title);
                    getActionBar().setTitle(title);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    Ln.d("onDrawerOpened Title %s", title);
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

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (!isTablet() && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
//                menuDrawer.toggleMenu();
                return true;
            /*case R.id.timer:
                navigateToTimer();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToHome() {
        final Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void navigateToTimer() {
        final Intent i = new Intent(this, BootstrapTimerActivity.class);
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
                // do nothing as we're already on the devices screen.
                break;
            case 3:
                // About
                navigateToAbout();
                break;
        }
    }
}
