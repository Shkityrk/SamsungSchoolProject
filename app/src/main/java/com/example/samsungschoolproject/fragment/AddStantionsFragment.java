package com.example.samsungschoolproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.adapter.AllStationAdapter;
import com.example.samsungschoolproject.databinding.AddStationItemBinding;
import com.example.samsungschoolproject.databinding.FragmentAddStantionsBinding;
import com.example.samsungschoolproject.fragment.viewmodel.StationsViewModel;
import com.example.samsungschoolproject.model.Station;

import java.util.ArrayList;
import java.util.List;

public class AddStantionsFragment extends Fragment {
    public FragmentAddStantionsBinding binding;
    private StationsViewModel mStationViewModel;

    public StationsViewModel getStationViewModel() {
        return mStationViewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAddStantionsBinding.inflate(getLayoutInflater());
        mStationViewModel = new ViewModelProvider(this).get(StationsViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_stantions, container, false);
        Button back_to_stations = (Button) view.findViewById(R.id.back_to_stations);
        Button done = (Button) view.findViewById(R.id.done);

        RecyclerView rv_add_station = view.findViewById(R.id.rv_add_stantion);
        rv_add_station.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false
        ));


        AllStationAdapter allStationAdapter = new AllStationAdapter(new AllStationAdapter.StationDiff());
        mStationViewModel.getAllWords().observe(getViewLifecycleOwner(), stationsList -> {
            allStationAdapter.submitList(stationsList);

            for (int i = 0; i < stationsList.size(); i++) {
                Station station = stationsList.get(i);
                boolean alarmState = station.getBoolAlarm();
                allStationAdapter.setSwitchState(alarmState, i);
            }
        });

        allStationAdapter.setSwitchChangeListener(new AllStationAdapter.OnSwitchChangeListener() {
            @Override
            public void onSwitchChanged(int position, boolean isChecked) {
                // Обновите значение в базе данных при изменении переключателя
                Station station = allStationAdapter.getCurrentList().get(position);
                station.setAlarm(isChecked);

            }
        });
        //----------------------------------------------

        rv_add_station.setAdapter(allStationAdapter);


        back_to_stations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StationsListFragment stationsListFragment = new StationsListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, stationsListFragment);

//                mStationViewModel.update(station);
                List<Station> stationsToUpdate = allStationAdapter.getCurrentList(); // Получить список станций для обновления
                mStationViewModel.updateStations(stationsToUpdate);

                transaction.commit();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Просто для теста. Добавится один раз, так как описана проверка по содержимому и политика игнорирования одинаковых
//                mStationViewModel.insert(new Station(13,"test","1", "0.0","0.0"));


                MainMenuFragment mainMenuFragment = new MainMenuFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, mainMenuFragment);

                List<Station> stationsToUpdate = allStationAdapter.getCurrentList(); // Получить список станций для обновления
                mStationViewModel.updateStations(stationsToUpdate);
                transaction.commit();
            }
        });
        TextView editTextSearch = view.findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                allStationAdapter.filterById(s.toString(), mStationViewModel.getAllWords().getValue());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        
        return view;
    }
}