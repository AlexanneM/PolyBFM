package fr.unice.polytech.polybfm;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DeclarationFragment extends Fragment {
    public DeclarationFragment() {}

    public static DeclarationFragment newInstance() {
        return new DeclarationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_declaration, container, false);

        return rootView;
    }
}
