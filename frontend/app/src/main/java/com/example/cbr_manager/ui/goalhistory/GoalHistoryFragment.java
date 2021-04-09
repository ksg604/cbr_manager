package com.example.cbr_manager.ui.goalhistory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.goal.Goal;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoalHistoryFragment extends Fragment {

    private static final int HEALTH_GOAL_KEY = 100;
    private static final int EDUCATION_GOAL_KEY = 101;
    private static final int SOCIAL_GOAL_KEY = 102;
    private static int goalType = -1;
    private static int clientId = -1;
    private APIService apiService = APIService.getInstance();
    private View view;
    private RecyclerView goalRecyclerView;

    public GoalHistoryFragment() {
        // Required empty public constructor
    }

    public static GoalHistoryFragment newInstance(String param1, String param2) {
        GoalHistoryFragment fragment = new GoalHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            goalType = bundle.getInt("GOAL_KEY", -1);
            clientId = bundle.getInt("CLIENT_ID", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_goal_history, container, false);
        getGoals();
        return view;
    }

    private void getGoals() {
        apiService.goalService.getGoals().enqueue(new Callback<List<Goal>>() {
            @Override
            public void onResponse(Call<List<Goal>> call, Response<List<Goal>> response) {
                if (response.isSuccessful()) {
                    List<Goal> goalArrayList = new ArrayList<>();
                    goalArrayList = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Goal>> call, Throwable t) {

            }
        });

    }
}