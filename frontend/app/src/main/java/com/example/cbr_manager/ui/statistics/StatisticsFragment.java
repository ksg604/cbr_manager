package com.example.cbr_manager.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.baseline_survey.BaselineSurvey;
import com.example.cbr_manager.service.client.Client;
import com.example.cbr_manager.service.referral.Referral;
import com.example.cbr_manager.service.visit.Visit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticsFragment extends Fragment {

    private static final int HIGH_RISK_THRESHOLD = 20;
    private final APIService apiService = APIService.getInstance();
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        setupStats(view);
        return view;
    }


    private void setupStats(View view) {
        if (apiService.isAuthenticated()) {
            setupReferralStats(view);
            setupClientStats(view);
            setupNumberOfVisits(view);
            setupBaselineSurveyStats(view);
        }
    }


    private void setupReferralStats(View view) {
        apiService.referralService.getReferrals().enqueue(new Callback<List<Referral>>() {
            @Override
            public void onResponse(Call<List<Referral>> call, Response<List<Referral>> response) {
                if (response.isSuccessful()) {
                    List<Referral> referrals = response.body();
                    int numReferrals = referrals.size();

                    TextView textView1 = view.findViewById(R.id.statistic4_sub2);
                    textView1.setText(Integer.toString(numReferrals));
                }
            }

            @Override
            public void onFailure(Call<List<Referral>> call, Throwable t) {

            }
        });
    }


    private void setupClientStats(View view) {
        apiService.clientService.getClients().enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful()) {
                    List<Client> clients = response.body();

                    int totalClients = clients.size();
                    double averageAge = getAverageAge(clients);
                    int totalFemales = getTotalFemales(clients);
                    int totalMales = getTotalMales(clients);
                    int totalHighRisk = getHighRiskClients(clients);
                    int totalNoCareGiver = getTotalNoCareGiver(clients);
                    double averageHealthRisk = getAverageHealthRisk(clients);
                    double averageEducationRisk = getAverageEducationRisk(clients);
                    double averageSocialRisk = getAverageSocialRisk(clients);
                    double averageRiskScore = getAverageRiskScore(clients);

                    TextView totalClientsView = view.findViewById(R.id.statistic1_sub1);
                    totalClientsView.setText(Integer.toString(totalClients));

                    TextView averageAgeView = view.findViewById(R.id.statistic1_sub2);
                    averageAgeView.setText(Double.toString(averageAge));

                    TextView totalFemalesView = view.findViewById(R.id.statistic1_sub3);
                    totalFemalesView.setText(Integer.toString(totalFemales));

                    TextView totalMalesView = view.findViewById(R.id.statistic1_sub4);
                    totalMalesView.setText(Integer.toString(totalMales));

                    TextView totalHighRiskView = view.findViewById(R.id.statistic1_sub5);
                    totalHighRiskView.setText(Integer.toString(totalHighRisk));

                    TextView totalNoCareGiverView = view.findViewById(R.id.statistic1_sub6);
                    totalNoCareGiverView.setText(Integer.toString(totalNoCareGiver));

                    TextView averageHealthRiskView = view.findViewById(R.id.statistic2_sub1);
                    averageHealthRiskView.setText(Double.toString(averageHealthRisk));

                    TextView averageEducationRiskView = view.findViewById(R.id.statistic2_sub2);
                    averageEducationRiskView.setText(Double.toString(averageEducationRisk));

                    TextView averageSocialRiskView = view.findViewById(R.id.statistic2_sub3);
                    averageSocialRiskView.setText(Double.toString(averageSocialRisk));

                    TextView averageRiskScoreView = view.findViewById(R.id.statistic2_sub4);
                    averageRiskScoreView.setText(Double.toString(averageRiskScore));
                }

            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {

            }
        });
    }


    private int getTotalNoCareGiver(List<Client> clients) {
        int totalNoCareGiver = 0;
        for (Client client : clients) {
            if (client.getCarePresent().toLowerCase().trim().equals("no")) {
                totalNoCareGiver++;
            }
        }
        return totalNoCareGiver;
    }


    private int getHighRiskClients(List<Client> clients) {
        int numHighRisk = 0;
        for (Client client : clients) {
            if (client.getRiskScore() >= HIGH_RISK_THRESHOLD) {
                numHighRisk++;
            }
        }
        return numHighRisk;
    }


    private int getTotalFemales(List<Client> clients) {
        int numFemales = 0;
        for (Client client : clients) {
            if (client.getGender().toLowerCase().trim().equals("female")) {
                numFemales++;
            }
        }
        return numFemales;
    }


    private int getTotalMales(List<Client> clients) {
        int numMales = 0;
        for (Client client : clients) {
            if (client.getGender().toLowerCase().trim().equals("male")) {
                numMales++;
            }
        }
        return numMales;
    }


    private double getAverageAge(List<Client> clients) {
        double averageAge = 0.0;
        for (Client client : clients) {
            averageAge += client.getAge();
        }
        return roundPrecision(averageAge / clients.size(), 1);
    }


    private double getAverageHealthRisk(List<Client> clients) {
        double averageHealthRisk = 0.0;
        for (Client client : clients) {
            averageHealthRisk += client.getHealthRisk();
        }
        return roundPrecision(averageHealthRisk / clients.size(), 1);
    }


    private double getAverageSocialRisk(List<Client> clients) {
        double averageSocialRisk = 0.0;
        for (Client client : clients) {
            averageSocialRisk += client.getSocialRisk();
        }
        return roundPrecision(averageSocialRisk / clients.size(), 1);
    }


    private double getAverageEducationRisk(List<Client> clients) {
        double averageEducationRisk = 0.0;
        for (Client client : clients) {
            averageEducationRisk += client.getEducationRisk();
        }
        return roundPrecision(averageEducationRisk / clients.size(), 1);
    }


    private double getAverageRiskScore(List<Client> clients) {
        double averageRiskScore = 0.0;
        for (Client client : clients) {
            averageRiskScore += client.getRiskScore();
        }
        return roundPrecision(averageRiskScore / clients.size(), 1);
    }


//  https://stackoverflow.com/questions/22186778/using-math-round-to-round-to-one-decimal-place
    private double roundPrecision(double num, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(num * scale) / scale;
    }


    private void setupBaselineSurveyStats(View view) {
        apiService.baselineSurveyService.getBaselineSurveys().enqueue(new Callback<List<BaselineSurvey>>() {
            @Override
            public void onResponse(Call<List<BaselineSurvey>> call, Response<List<BaselineSurvey>> response) {
                if (response.isSuccessful()) {
                    List<BaselineSurvey> surveys = response.body();

                    int totalVeryPoor = getTotalVeryPoor(surveys);
                    int totalPoor = getTotalPoor(surveys);
                    int totalFine = getTotalFine(surveys);
                    int totalGood = getTotalGood(surveys);

                    TextView totalVeryPoorView = view.findViewById(R.id.statistic3_sub1);
                    totalVeryPoorView.setText(Integer.toString(totalVeryPoor));

                    TextView totalPoorView = view.findViewById(R.id.statistic3_sub2);
                    totalPoorView.setText(Integer.toString(totalPoor));

                    TextView totalFineView = view.findViewById(R.id.statistic3_sub3);
                    totalFineView.setText(Integer.toString(totalFine));

                    TextView totalGoodView = view.findViewById(R.id.statistic3_sub4);
                    totalGoodView.setText(Integer.toString(totalGood));
                }

            }

            @Override
            public void onFailure(Call<List<BaselineSurvey>> call, Throwable t) {

            }
        });
    }


    private int getTotalVeryPoor(List<BaselineSurvey> surveys) {
        int totalVeryPoor = 0;
        for (BaselineSurvey survey : surveys) {
            if (survey.getGeneralHealth().toLowerCase().trim().equals("very poor")) {
                totalVeryPoor++;
            }
        }
        return totalVeryPoor;
    }


    private int getTotalPoor(List<BaselineSurvey> surveys) {
        int totalPoor = 0;
        for (BaselineSurvey survey : surveys) {
            if (survey.getGeneralHealth().toLowerCase().trim().equals("poor")) {
                totalPoor++;
            }
        }
        return totalPoor;
    }


    private int getTotalFine(List<BaselineSurvey> surveys) {
        int totalFine = 0;
        for (BaselineSurvey survey : surveys) {
            if (survey.getGeneralHealth().toLowerCase().trim().equals("fine")) {
                totalFine++;
            }
        }
        return totalFine;
    }


    private int getTotalGood(List<BaselineSurvey> surveys) {
        int totalGood = 0;
        for (BaselineSurvey survey : surveys) {
            if (survey.getGeneralHealth().toLowerCase().trim().equals("good")) {
                totalGood++;
            }
        }
        return totalGood;
    }
    

    private void setupNumberOfVisits(View view) {
        apiService.visitService.getVisits().enqueue(new Callback<List<Visit>>() {
            @Override
            public void onResponse(Call<List<Visit>> call, Response<List<Visit>> response) {
                if (response.isSuccessful()) {
                    List<Visit> visits = response.body();
                    int numVisits = visits.size();

                    TextView numVisitsView = view.findViewById(R.id.statistic4_sub1);
                    numVisitsView.setText(Integer.toString(numVisits));
                }
            }

            @Override
            public void onFailure(Call<List<Visit>> call, Throwable t) {

            }
        });
    }


}
