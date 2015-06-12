package ch.bfh.mobicomp.leddr.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.kevinsawicki.wishlist.Toaster;

import butterknife.InjectView;
import ch.bfh.mobicomp.leddr.R;
import ch.bfh.mobicomp.leddr.db.LeddrContract;
import ch.bfh.mobicomp.leddr.db.LeddrDbHelper;

/**
 * Created by sw on 22.05.2015.
 */
public class AddDeviceActivity extends BootstrapFragmentActivity {

    @InjectView(R.id.add) protected Button add;
    @InjectView(R.id.addDeviceName) protected EditText addDeviceName;
    @InjectView(R.id.addDeviceId) protected EditText addDeviceId;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_device);

        setTitle(R.string.title_add_device);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeddrDbHelper leddrDbHelper = new LeddrDbHelper(AddDeviceActivity.this.getApplicationContext());

                // Gets the data repository in write mode
                SQLiteDatabase db = leddrDbHelper.getWritableDatabase();


                String uid = String.format("%s%s", "$5G2Fdfh66hsdfDFB#(54", 0);
                String name = addDeviceName.getText().toString();
                String id = addDeviceId.getText().toString();

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

                Toaster.showShort(AddDeviceActivity.this,"added " + name + " to the DB");

                Intent intent  = new Intent(AddDeviceActivity.this.getApplicationContext(), MainActivity.class);
                intent.putExtra("DeviceName", name);
                intent.putExtra("DeviceId", id);
                startActivity(intent);
            }
        });
    }

}
