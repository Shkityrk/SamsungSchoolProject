package com.example.samsungschoolproject.fragment;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.activity.SettingsMenuActivity;
import com.example.samsungschoolproject.service.LocationService;
import com.example.samsungschoolproject.utils.SharedPreferencesUtils;

import java.util.Objects;


public class MainMenuFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setId(R.id.container_1);
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getContext());
        if(sharedPreferencesUtils.getServiceRunning()){
            stopLocationService();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        Button settingsButton = (Button) view.findViewById(R.id.settings_button);
        Button chooseStationButton = (Button) view.findViewById(R.id.choose_station_button) ;
        Button start = (Button) view.findViewById(R.id.start_button);

        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getContext());

        if (sharedPreferencesUtils.getServiceRunning()){
            start.setText("Выключить оповещения");

        } else {
            start.setText("Включить оповещения");

        }

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SettingsMenuActivity.class);
                startActivity(intent);
            }
        });

        chooseStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavouriteStationsListFragment favouriteStationsListFragment = new FavouriteStationsListFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, favouriteStationsListFragment);


                transaction.commit();
            }
        });

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = getString(R.string.main_button_push);
                Toast.makeText(v.getContext(), name, Toast.LENGTH_SHORT).show();
                SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getContext());

                if (sharedPreferencesUtils.getServiceRunning()){
                    start.setText("Включить оповещения");
                    Log.d("Service", "onClick: " + sharedPreferencesUtils.getServiceRunning() + " so, stop");
                    stopLocationService();
                } else {
                    start.setText("Выключить оповещения");
                    Log.d("Service", "onClick: " + sharedPreferencesUtils.getServiceRunning() + " so, start");
                    startLocationService();
                }
            }
        });
        return view;
    }

    public void startLocationService() {
        Intent serviceIntent = new Intent(getActivity(), LocationService.class);
        ContextCompat.startForegroundService(requireActivity(), serviceIntent);
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getContext());
        sharedPreferencesUtils.setServiceRunning(true);
        Log.d("IsRunning", "true");
    }

    public void stopLocationService() {
        Intent serviceIntent = new Intent(getActivity(), LocationService.class);
        requireActivity().stopService(serviceIntent);
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getContext());
        sharedPreferencesUtils.setServiceRunning(false);
        Log.d("IsRunning", "false");
    }
}