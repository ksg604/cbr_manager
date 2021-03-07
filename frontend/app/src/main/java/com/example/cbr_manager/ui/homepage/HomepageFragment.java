package com.example.cbr_manager.ui.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cbr_manager.R;
import com.example.cbr_manager.ui.clientlist.ClientListFragment;
import com.example.cbr_manager.ui.clientselector.ClientSelectorActivity;
import com.example.cbr_manager.ui.create_client.CreateClientActivity;
import com.example.cbr_manager.ui.dashboard.DashboardFragment;

public class HomepageFragment extends Fragment {
    private ImageButton newClientButton, newVisitButton, dashboardButton;
    private ImageButton newReferralButton, clientListButton, syncButton;
    private final int NEW_VISIT_CODE = 100;
    private final int NEW_REFERRAL_CODE = 101;

    View view;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage, container, false);

        newClientButton = view.findViewById(R.id.newClientButton);
        newClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateClientActivity.class);
                startActivity(intent);
            }
        });

        newVisitButton = view.findViewById(R.id.newVisitButton);
        newVisitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(HomepageActivity.this, CreateVisitActivity.class);
                Intent intent = new Intent(view.getContext(), ClientSelectorActivity.class);
                intent.putExtra("CODE", NEW_VISIT_CODE);
                startActivity(intent);
            }
        });

        dashboardButton = view.findViewById(R.id.dashboardButton);
        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomepageFragment.this)
                        .navigate(R.id.action_nav_home_to_nav_dashboard);
            }
        });

        newReferralButton = view.findViewById(R.id.newReferralButton);
        newReferralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ClientSelectorActivity.class);
                intent.putExtra("CODE", NEW_REFERRAL_CODE);
                startActivity(intent);
            }
        });

        clientListButton = view.findViewById(R.id.clientListButton);
        clientListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(HomepageFragment.this)
                        .navigate(R.id.action_nav_home_to_nav_client_list);
            }
        });

        syncButton = view.findViewById(R.id.syncButton);


        return view;
    }



}