package ch.bfh.mobicomp.leddr.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.github.kevinsawicki.wishlist.Toaster;

import ch.bfh.mobicomp.leddr.R;
import ch.bfh.mobicomp.leddr.core.DeviceContentProvider;
import ch.bfh.mobicomp.leddr.db.LeddrContract;

public class DeviceListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DEVICE_LIST_LOADER = 0x01;
    private static final String[] DEVICE_PROJECTION = new String[] {
            LeddrContract.DeviceEntry._ID,
            LeddrContract.DeviceEntry.COLUMN_NAME_NAME,
            LeddrContract.DeviceEntry.COLUMN_NAME_DEVICE_ID,
            LeddrContract.DeviceEntry.COLUMN_NAME_USER_ID
    };

    // This is the Adapter being used to display the list's data.
    SimpleCursorAdapter adapter;

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Give some text to display if there is no data. In a real
        // application this would come from a resource.
        setEmptyText(getString(R.string.no_devices));

        String[] uiBindFrom = {
                LeddrContract.DeviceEntry.COLUMN_NAME_NAME,
                LeddrContract.DeviceEntry.COLUMN_NAME_DEVICE_ID,
        };
        int[] uiBindTo = { android.R.id.text1, android.R.id.text2 };

        // Create an empty adapter we will use to display the loaded data.
        adapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_2, null,
                uiBindFrom,
                uiBindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
        setListAdapter(adapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(DEVICE_LIST_LOADER, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor deviceCursor = getActivity().getContentResolver().query(
                Uri.withAppendedPath(DeviceContentProvider.CONTENT_URI,
                        String.valueOf(id)), DEVICE_PROJECTION, null, null, null);
        if (deviceCursor.moveToFirst()) {
            String deviceName = deviceCursor.getString(
                    deviceCursor.getColumnIndex(LeddrContract.DeviceEntry.COLUMN_NAME_NAME)
            );
            // Start the detail device activity or replace the fragment here...
            Toaster.showShort(getActivity(), deviceName);
        }
        deviceCursor.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                DeviceContentProvider.CONTENT_URI, DEVICE_PROJECTION, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
