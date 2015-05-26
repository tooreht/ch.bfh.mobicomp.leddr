package ch.bfh.mobicomp.leddr.ui;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.bfh.mobicomp.leddr.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UnitMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UnitMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitMainFragment extends Fragment {

    // the fragment initialization parameter
    private static final String UNIT = "unit";
    private String unit;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * //@param unit the led unit to be displayed.
     * @return A new instance of fragment UnitMainFragment.
     */
    public static UnitMainFragment newInstance() {
        UnitMainFragment fragment = new UnitMainFragment();
        Bundle args = new Bundle();
        //args.putString(UNIT, unit);
        fragment.setArguments(args);
        return fragment;
    }

    public UnitMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            unit = getArguments().getString(UNIT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unit_main, container, false);
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
