package id.bizdir.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.bizdir.R;

/**
 * Created by Hendry on 20/04/2015.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String sectionName;
    private int sectionNumber;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public PlaceholderFragment newInstance(int sectionNumber, String sectionName) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.sectionName = sectionName;
        fragment.sectionNumber = sectionNumber;
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        if (savedInstanceState != null) {
            sectionName = (String) savedInstanceState.getSerializable("sectionName");
            sectionNumber = (int) savedInstanceState.getSerializable("sectionNumber");
        }

        TextView section_label = (TextView) rootView.findViewById(R.id.section_label);
        section_label.setText("Sorry, " + sectionName + " is still under construction. Please check back later!");
        return rootView;
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.size() > 0) {
            menu.clear();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
    */

    /*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
    */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("sectionName", sectionName);
        outState.putInt("sectionNumber", sectionNumber);
    }
}
