package ch.bfh.mobicomp.leddr.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Hashtable;
import java.util.Locale;

import ch.bfh.mobicomp.leddr.R;

/**
 * created by sw
 */
public class AboutActivity extends BootstrapFragmentActivity {
    Button btn;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about);

        setTitle(R.string.title_about);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
