package fr.unice.polytech.polybfm;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by Baptiste on 12/05/2018.
 */

public class TestNotifFragment extends Fragment {

    public TestNotifFragment(){}

    public static TestNotifFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TestNotifFragment fragment = new TestNotifFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_testnotif, container, false);
        Button button = rootView.findViewById(R.id.buttonTest);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NotificationHelper notificationHelper = new NotificationHelper(getActivity().getApplicationContext(),getResources().getString(R.string.notif_titre),getResources().getString(R.string.notif_texte));
                notificationHelper.sendNotification();
            }
        });

        return rootView;
    }
}