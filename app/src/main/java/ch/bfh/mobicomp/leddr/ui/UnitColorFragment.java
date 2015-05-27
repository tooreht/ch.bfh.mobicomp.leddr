package ch.bfh.mobicomp.leddr.ui;

import android.app.Fragment;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import ch.bfh.mobicomp.leddr.util.RGBListener;
import ch.bfh.mobicomp.leddr.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ch.bfh.mobicomp.leddr.ui.UnitColorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ch.bfh.mobicomp.leddr.ui.UnitColorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitColorFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageView colorField;
    private SeekBar sBarRed;
    private SeekBar sBarGreen;
    private SeekBar sBarBlue;
    private RGBListener redListener;
    private RGBListener greenListener;
    private RGBListener blueListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UnitMainFragment.
     */
    public static UnitColorFragment newInstance() {
        UnitColorFragment fragment = new UnitColorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public UnitColorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View colorParentView = inflater.inflate(R.layout.fragment_unit_color, container, false);

        colorField = (ImageView) colorParentView.findViewById(R.id.colorView);
        colorField.setBackgroundColor(Color.rgb(0,0,0));

        sBarRed = (SeekBar) colorParentView.findViewById(R.id.seekBarRed);
        redListener = new RGBListener(colorField,RGBListener.REDCOMPONENT);
        sBarRed.setOnSeekBarChangeListener(redListener);

        sBarGreen = (SeekBar) colorParentView.findViewById(R.id.seekBarGreen);
        greenListener = new RGBListener(colorField,RGBListener.GREENCOMPONENT);
        sBarGreen.setOnSeekBarChangeListener(greenListener);

        sBarBlue = (SeekBar) colorParentView.findViewById(R.id.seekBarBlue);
        blueListener = new RGBListener(colorField,RGBListener.BLUECOMPONENT);
        sBarBlue.setOnSeekBarChangeListener(blueListener);

        return colorParentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
