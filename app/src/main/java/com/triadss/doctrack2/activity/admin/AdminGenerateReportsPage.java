package com.triadss.doctrack2.activity.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triadss.doctrack2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminGenerateReportsPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminGenerateReportsPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminGenerateReportsPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminGenerateReportsPage.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminGenerateReportsPage newInstance(String param1, String param2) {
        AdminGenerateReportsPage fragment = new AdminGenerateReportsPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_generate_reports_page, container, false); 

        Button generateReports = rootView.findViewById(R.id.generate_report_button);
        DatePicker startDatePicker = rootView.findViewById(R.id.start_date_picker);
        DatePicker endDatePicker = rootView.findViewById(R.id.end_date_picker);

        generateReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp startDate = DateDto.fromDatePicker(startDatePicker).ToTimeStamp();
                Timestamp endDate = DateDto.fromDatePicker(endDatePicker).ToTimeStamp(); 
                showReportDialog(startDate, endDate);
            }
        });

        return rootView;
    }

    private void showReportDialog(Timestamp startDate, Timestamp endDate) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_generate_reports);
        
        RecyclerView recyclerView = dialog.findViewById(R.id.reports_recycler_view);
        LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        List<ReportDto> retrievedReports = new ArrayList<>();

        ReportsRepository reportsRepository = new ReportsRepository();

        reportsRepository.getReportsFromDateRange(startDate, endDate, new ReportsRepository.ReportsFetchCallback() {
            @Override
            public void onSuccess(List<ReportDto> reports) {
                retrievedReports = reports;
                recyclerView.setAdapter(new AdminGenerateReportAdapter(getContext(), retrievedReports));
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        TextInputEditText editTextSearch = rootView.findViewById(R.id.search_bar_patient);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String filter = s.toString();
                recyclerView.setAdapter(new AdminGenerateReportAdapter(getContext(), 
                    retrievedReports.stream()
                        .filter(report -> report.getMessage().contains(filter) && 
                            report.getAction().contains(filter) &&
                            report.getCreatedByName().contains(filter) 
                        ).collect(Colectors.toList())
                    ));
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        dialog.show();

    }

}