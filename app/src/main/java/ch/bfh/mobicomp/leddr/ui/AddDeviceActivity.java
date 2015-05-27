package ch.bfh.mobicomp.leddr.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.InjectView;
import ch.bfh.mobicomp.leddr.R;

/**
 * Created by sw on 22.05.2015.
 */
public class AddDeviceActivity extends BootstrapFragmentActivity {

    @InjectView(R.id.add) protected Button add;


    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_device);

        setTitle(R.string.title_add_device);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        add.setOnClickListener(this);
    }



    @Override
    public void onClick(final View v) {

    }


}
