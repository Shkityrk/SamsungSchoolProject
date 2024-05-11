package com.example.samsungschoolproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.adapter.FavouriteStationAdapter;
import com.example.samsungschoolproject.fragment.viewmodel.FavouriteViewModel;
import com.example.samsungschoolproject.model.Station;

import java.util.List;

public class FavouriteStationsListFragment extends Fragment {
    private FavouriteViewModel mStationViewModel;
    public FavouriteViewModel getStationViewModel() {
        return mStationViewModel;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStationViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stations_list, container, false);
        Button back_to_main_menu_button= (Button) view.findViewById(R.id.back_to_main_menu_button);

        Button create_new_alarm = (Button) view.findViewById(R.id.create_new_alarm);
        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false
        ));

        FavouriteStationAdapter favouriteStationAdapter = new FavouriteStationAdapter(new FavouriteStationAdapter.StationDiff());

        //мусор, нужен был зоядля теста :)
//        favouriteStationAdapter.Add(new Station(1,"Юго-западная", "Сокольническая линия","null","null","null","null","null","null","null","null"));
//        favouriteStationAdapter.Add(new Station(2,"Коптево", "МЦК","null","null" ,"null","null","null","null","null","null"));
//        favouriteStationAdapter.Add(new Station(3,"Проспект Вернадского", "МЦК-Сокольническая линия","null","null","null","null","null","null","null","null"));
//        favouriteStationAdapter.Add(new Station(4,"Киевская", "Кольцевая линия-Арбатско-покровская линия-Филевская линия","null","null" ,"null","null","null","null","null","null"));

        mStationViewModel.getAllWords().observe(getViewLifecycleOwner(), stationsList -> {
            favouriteStationAdapter.submitList(stationsList);

            for (int i = 0; i < stationsList.size(); i++) {
                Station station = stationsList.get(i);
                boolean alarmState = station.getBoolAlarm();
                favouriteStationAdapter.setSwitchState(alarmState, i);
            }
        });
        favouriteStationAdapter.setSwitchChangeListener(new FavouriteStationAdapter.OnSwitchChangeListener(){
        @Override
        public void onSwitchChanged(int position, boolean isChecked) {
            // Обновите значение в базе данных при изменении переключателя
            Station station = favouriteStationAdapter.getCurrentList().get(position);
            station.setAlarm(isChecked);

        }
    });

        rv.setAdapter(favouriteStationAdapter);





        back_to_main_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuFragment mainMenuFragment = new MainMenuFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, mainMenuFragment);

                List<Station> stationsToUpdate =favouriteStationAdapter.getCurrentList(); // Получить список станций для обновления
                mStationViewModel.updateStations(stationsToUpdate);

                transaction.commit();
            }
        });

        create_new_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFavouriteStationsFragment addFavouriteStationsFragment = new AddFavouriteStationsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, addFavouriteStationsFragment);

                List<Station> stationsToUpdate =favouriteStationAdapter.getCurrentList(); // Получить список станций для обновления
                mStationViewModel.updateStations(stationsToUpdate);

                transaction.commit();
            }
        });


        return view;

    }


//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        MainMenuFragment mainMenuFragment = new MainMenuFragment();
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.container, mainMenuFragment);
//
//
//        List<Station> stationsToUpdate =favouriteStationAdapter.getCurrentList(); // Получить список станций для обновления
//        mStationViewModel.updateStations(stationsToUpdate);
//
//        transaction.commit();
//    }




}