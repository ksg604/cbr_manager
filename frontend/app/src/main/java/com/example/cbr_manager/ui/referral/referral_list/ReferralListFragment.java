package com.example.cbr_manager.ui.referral.referral_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.referral.Referral;
import com.example.cbr_manager.ui.referral.referral_details.ReferralDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferralListFragment extends Fragment implements ReferralListRecyclerItemAdapter.OnItemListener{

    private RecyclerView referralListRecyclerView;
    private ReferralListRecyclerItemAdapter adapter;
    private RecyclerView.LayoutManager referralListLayoutManager;
    private SearchView searchView;
    private CheckBox checkBox;
    private int clientId = -1;
    private final int FROM_DASHBOARD = -1;
    ArrayList<ReferralListRecyclerItem> referralRecyclerItems = new ArrayList<>();;

    private APIService apiService = APIService.getInstance();

    @Override
    public void onResume() {
        super.onResume();
        fetchReferralsToList(referralRecyclerItems);

        if(clientId==FROM_DASHBOARD){
            checkBox.setChecked(true);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_referral_list, container, false);

        if(getArguments()!=null){
        clientId = getArguments().getInt("CLIENT_ID", -1);}

        referralListRecyclerView = root.findViewById(R.id.recyclerView);
        referralListRecyclerView.setHasFixedSize(true); // if we know it won't change size.
        referralListLayoutManager = new LinearLayoutManager(getContext());
        adapter = new ReferralListRecyclerItemAdapter(referralRecyclerItems, this);
        referralListRecyclerView.setLayoutManager(referralListLayoutManager);
        referralListRecyclerView.setAdapter(adapter);


        checkBox = root.findViewById(R.id.checkBox);
        searchView = root.findViewById(R.id.referralSearchView);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CharSequence newText = searchView.getQuery();
                adapter.getFilterWithCheckBox(checkBox.isChecked()).filter(newText);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilterWithCheckBox(checkBox.isChecked()).filter(newText);
                return true;
            }
        });

        return root;
    }

    public void fetchReferralsToList(List<ReferralListRecyclerItem> referralUIList) {
        if (apiService.isAuthenticated()) {
//            referralUIList.clear();
//            adapter.notifyDataSetChanged();
            apiService.referralService.getReferrals().enqueue(new Callback<List<Referral>>() {
                @Override
                public void onResponse(Call<List<Referral>> call, Response<List<Referral>> response) {
                    if (response.isSuccessful()) {
                        List<Referral> referralList = response.body();
                        for (Referral referral : referralList) {
                            if(referral.getClient()==clientId| clientId<0){
                            referralUIList.add(new ReferralListRecyclerItem(referral.getStatus(), referral.getServiceType(), referral.getRefer_to(), referral, referral.getDateCreated(),referral.getClient()));
                            }
                        }
                        adapter.getFilterWithCheckBox(checkBox.isChecked()).filter(searchView.getQuery());
                    }
                }

                @Override
                public void onFailure(Call<List<Referral>> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent referralInfoIntent = new Intent(getContext(), ReferralDetailsActivity.class);

        ReferralListRecyclerItem referralListRecyclerItem = adapter.getReferral(position);
        referralInfoIntent.putExtra("referralId", referralListRecyclerItem.getReferral().getId());

        startActivity(referralInfoIntent);
    }
}