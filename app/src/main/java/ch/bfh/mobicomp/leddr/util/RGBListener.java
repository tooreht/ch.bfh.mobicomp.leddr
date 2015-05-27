package ch.bfh.mobicomp.leddr.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * Created by sellf1
 */
public class RGBListener implements SeekBar.OnSeekBarChangeListener {

    ImageView colorField;
    int value;
    String rgbComponent;
    int fieldColor;
    ColorDrawable drawable;

    public static String REDCOMPONENT = "RED";
    public static String GREENCOMPONENT = "GREEN";
    public static String BLUECOMPONENT = "BLUE";

    public RGBListener(ImageView colorField, String rgbComponent){
        this.colorField = colorField;
        this.rgbComponent = rgbComponent;
        this.drawable = new ColorDrawable();
        this.fieldColor = 0;
        this.value = 0;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        value = (int) (progress * 2.554);
        if(rgbComponent.equals(REDCOMPONENT)){
            drawable = (ColorDrawable) colorField.getBackground();
            fieldColor = drawable.getColor();
            colorField.setBackgroundColor(Color.rgb(value,Color.green(fieldColor),Color.blue(fieldColor)));
        }
        if(rgbComponent.equals(GREENCOMPONENT)){
            drawable = (ColorDrawable) colorField.getBackground();
            fieldColor = drawable.getColor();
            colorField.setBackgroundColor(Color.rgb(Color.red(fieldColor),value,Color.blue(fieldColor)));
        }
        if(rgbComponent.equals(BLUECOMPONENT)){
            drawable = (ColorDrawable) colorField.getBackground();
            fieldColor = drawable.getColor();
            colorField.setBackgroundColor(Color.rgb(Color.red(fieldColor),Color.green(fieldColor),value));
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
