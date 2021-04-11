package com.example.cbr_manager.ui.alert.alert_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.alert.Alert;
import com.example.cbr_manager.ui.AlertViewModel;
import com.example.cbr_manager.ui.alert.alert_details.AlertDetailsActivity;
import com.example.cbr_manager.utils.Helper;

import org.threeten.bp.format.FormatStyle;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class AlertListFragment extends Fragment implements AlertListRecyclerItemAdapter.OnItemListener{

    private RecyclerView alertListRecyclerView;
    private AlertListRecyclerItemAdapter adapter;
    private RecyclerView.LayoutManager alertLayoutManager;
    private SearchView searchView;
    private AlertViewModel alertViewModel;
    ArrayList<AlertListRecyclerItem> alertRecyclerItems = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alert_list, container, false);

        alertViewModel = new ViewModelProvider(this).get(AlertViewModel.class);

        alertListRecyclerView = root.findViewById(R.id.recyclerView);
        alertListRecyclerView.setHasFixedSize(true); // if we know it won't change size.
        alertLayoutManager = new LinearLayoutManager(getContext());
        adapter = new AlertListRecyclerItemAdapter(alertRecyclerItems, this);
        alertListRecyclerView.setLayoutManager(alertLayoutManager);
        alertListRecyclerView.setAdapter(adapter);
        fetchAlertsToList(alertRecyclerItems);

        SearchView alertSearchView = root.findViewById(R.id.alertSearchView);
        alertSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return root;
    }

    public void fetchAlertsToList(List<AlertListRecyclerItem> alertUIList) {
        alertViewModel.getAllAlerts().observe(getViewLifecycleOwner(), alerts -> {
            alertUIList.clear();
            for (Alert alert : alerts) {
                alertUIList.add(new AlertListRecyclerItem(alert.getTitle(), alert.getBody(), alert, Helper.formatDateTimeToLocalString(alert.getDate(), FormatStyle.SHORT)));
            }
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent alertInfoIntent = new Intent(getContext(), AlertDetailsActivity.class);
        AlertListRecyclerItem alertListRecyclerItem = adapter.getAlert(position);
        alertInfoIntent.putExtra("alertId", alertListRecyclerItem.getAlert().getId());
        startActivity(alertInfoIntent);
    }
}