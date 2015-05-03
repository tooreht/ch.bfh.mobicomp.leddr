package ch.bfh.mobicomp.leddr;

import android.accounts.AccountManager;
import android.content.Context;

import ch.bfh.mobicomp.leddr.authenticator.ApiKeyProvider;
import ch.bfh.mobicomp.leddr.authenticator.BootstrapAuthenticatorActivity;
import ch.bfh.mobicomp.leddr.authenticator.LogoutService;
import ch.bfh.mobicomp.leddr.core.BootstrapService;
import ch.bfh.mobicomp.leddr.core.Constants;
import ch.bfh.mobicomp.leddr.core.PostFromAnyThreadBus;
import ch.bfh.mobicomp.leddr.core.RestAdapterRequestInterceptor;
import ch.bfh.mobicomp.leddr.core.RestErrorHandler;
import ch.bfh.mobicomp.leddr.core.TimerService;
import ch.bfh.mobicomp.leddr.core.UserAgentProvider;
import ch.bfh.mobicomp.leddr.ui.AboutActivity;
import ch.bfh.mobicomp.leddr.ui.BootstrapTimerActivity;
import ch.bfh.mobicomp.leddr.ui.CheckInsListFragment;
import ch.bfh.mobicomp.leddr.ui.DeviceActivity;
import ch.bfh.mobicomp.leddr.ui.MainActivity;
import ch.bfh.mobicomp.leddr.ui.NavigationDrawerFragment;
import ch.bfh.mobicomp.leddr.ui.NewsActivity;
import ch.bfh.mobicomp.leddr.ui.NewsListFragment;
import ch.bfh.mobicomp.leddr.ui.UserActivity;
import ch.bfh.mobicomp.leddr.ui.UserListFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module(
        complete = false,

        injects = {
                BootstrapApplication.class,
                BootstrapAuthenticatorActivity.class,
                MainActivity.class,
                BootstrapTimerActivity.class,
                CheckInsListFragment.class,
                NavigationDrawerFragment.class,
                NewsActivity.class,
                NewsListFragment.class,
                UserActivity.class,
                UserListFragment.class,
                TimerService.class,
                DeviceActivity.class,
                AboutActivity.class
        }
)
public class BootstrapModule {

    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }

    @Provides
    @Singleton
    LogoutService provideLogoutService(final Context context, final AccountManager accountManager) {
        return new LogoutService(context, accountManager);
    }

    @Provides
    BootstrapService provideBootstrapService(RestAdapter restAdapter) {
        return new BootstrapService(restAdapter);
    }

    @Provides
    BootstrapServiceProvider provideBootstrapServiceProvider(RestAdapter restAdapter, ApiKeyProvider apiKeyProvider) {
        return new BootstrapServiceProvider(restAdapter, apiKeyProvider);
    }

    @Provides
    ApiKeyProvider provideApiKeyProvider(AccountManager accountManager) {
        return new ApiKeyProvider(accountManager);
    }

    @Provides
    Gson provideGson() {
        /**
         * GSON instance to use for all request  with date format set up for proper parsing.
         * <p/>
         * You can also configure GSON with different naming policies for your API.
         * Maybe your API is Rails API and all json values are lower case with an underscore,
         * like this "first_name" instead of "firstName".
         * You can configure GSON as such below.
         * <p/>
         *
         * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd")
         *         .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
         */
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Provides
    RestErrorHandler provideRestErrorHandler(Bus bus) {
        return new RestErrorHandler(bus);
    }

    @Provides
    RestAdapterRequestInterceptor provideRestAdapterRequestInterceptor(UserAgentProvider userAgentProvider) {
        return new RestAdapterRequestInterceptor(userAgentProvider);
    }

    @Provides
    RestAdapter provideRestAdapter(RestErrorHandler restErrorHandler, RestAdapterRequestInterceptor restRequestInterceptor, Gson gson) {
        return new RestAdapter.Builder()
                .setEndpoint(Constants.Http.URL_BASE)
                .setErrorHandler(restErrorHandler)
                .setRequestInterceptor(restRequestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }

}
