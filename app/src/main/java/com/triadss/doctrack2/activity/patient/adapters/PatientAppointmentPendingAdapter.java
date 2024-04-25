package com.triadss.doctrack2.activity.patient.adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.ReportConstants;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.DateDto;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.TimeDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import com.triadss.doctrack2.helper.ButtonManager;
import com.triadss.doctrack2.repoositories.AppointmentRepository;
import com.triadss.doctrack2.repoositories.NotificationRepository;
import com.triadss.doctrack2.utils.AppointmentFunctions;

import org.w3c.dom.Text;

// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class PatientAppointmentPendingAdapter
        extends RecyclerView.Adapter<PatientAppointmentPendingAdapter.ViewHolder> {
    AppointmentRepository appointmentRepository;
    ArrayList<AppointmentDto> appointments;
    Context context;
    NotificationRepository notificationRepository = new NotificationRepository();
    private Button cancel;

    private String TimePick;
    public String getTimePick(){
//        if(TimePick.equals("8:00 am - 9:00 am")){
//            this.TimePick = "8:00";
//        }
//        if(TimePick.equals("9:00 am - 10:00 am")){
//            this.TimePick = "9:00";
//        }
//        if(TimePick.equals("10:00 am - 11:00 am")){
//            this.TimePick = "10:00";
//        }
//        if(TimePick.equals("11:00 am - 12:00 pm")){
//            this.TimePick = "11:00";
//        }
//        if(TimePick.equals("12:00 pm - 1:00 pm")){
//            this.TimePick = "12:00";
//        }
//        if(TimePick.equals("1:00 pm - 2:00 pm")){
//            this.TimePick = "1:00";
//        }
//        if(TimePick.equals("2:00 pm - 3:00 pm")){
//            this.TimePick = "2:00";
//        }
//        if(TimePick.equals("3:00 pm - 4:00 pm")){
//            this.TimePick = "3:00";
//        }
//        if(TimePick.equals("4:00 pm - 5:00 pm")){
//            this.TimePick = "4:00";
//        }
        return TimePick;
    }

    public void setTimePick(String TimePick){
        if(TimePick.equals("8:00 am - 9:00 am")){
            TimePick = "8:00";
        }
        if(TimePick.equals("9:00 am - 10:00 am")){
            TimePick = "9:00";
        }
        if(TimePick.equals("10:00 am - 11:00 am")){
            TimePick ="10:00";
        }
        if(TimePick.equals("11:00 am - 12:00 pm")){
            TimePick = "11:00";
        }
        if(TimePick.equals("12:00 pm - 1:00 pm")){
            TimePick = "12:00";
        }
        if(TimePick.equals("1:00 pm - 2:00 pm")){
            TimePick = "1:00";
        }
        if(TimePick.equals("2:00 pm - 3:00 pm")){
            TimePick = "2:00";
        }
        if(TimePick.equals("3:00 pm - 4:00 pm")){
            TimePick = "3:00";
        }
        if(TimePick.equals("4:00 pm - 5:00 pm")){
            TimePick = "4:00";
        }
        this.TimePick = TimePick;
    }
    // Constructor for initialization
    public PatientAppointmentPendingAdapter(Context context, ArrayList<AppointmentDto> appointments) {
        this.context = context;

        this.appointments = appointments;
    }

    @NonNull
    @Override
    public PatientAppointmentPendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        appointmentRepository = new AppointmentRepository();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_appointment_pending, parent, false);

        // Passing view to ViewHolder
        PatientAppointmentPendingAdapter.ViewHolder viewHolder = new PatientAppointmentPendingAdapter.ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull PatientAppointmentPendingAdapter.ViewHolder holder, int position) {
        // TypeCast Object to int type
        holder.update(appointments.get(position));
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView purpose, date, time, documentId, patientName;
        private Button reschedule, cancel;

        public ViewHolder(View view) {
            super(view);
            purpose = (TextView) view.findViewById(R.id.purposetext);
            date = (TextView) view.findViewById(R.id.appointment_date);
            time = (TextView) view.findViewById(R.id.appointment_time);
            cancel = (Button) itemView.findViewById(R.id.cancel_button);
            reschedule = (Button) itemView.findViewById(R.id.reschedule_button);
            documentId = (TextView) view.findViewById(R.id.IDtext);
            patientName = view.findViewById(R.id.nametext);
        }

        public void update(AppointmentDto appointment) {
            purpose.setText(appointment.getPurpose());
            DateTimeDto dateTimeDto = DateTimeDto.ToDateTimeDto(appointment.getDateOfAppointment());
            date.setText(dateTimeDto.getDate().ToString());

            DateTimeDto rangeEnd = dateTimeDto.Clone();
            TimeDto startTime = dateTimeDto.getTime();
            rangeEnd.setTime(new TimeDto(startTime.getHour() + 1, startTime.getMinute()));

            time.setText(String.format("%s - %s", dateTimeDto.getTime().ToString(), rangeEnd.getTime().ToString()));
            documentId.setText(appointment.getPatientIdNumber());
            patientName.setText(appointment.getNameOfRequester());

            reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), purpose.getText(), Toast.LENGTH_SHORT).show();
                    showUpdateDialog(appointment);
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), appointment.getDocumentId(), Toast.LENGTH_SHORT).show();
                    android.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(itemView.getContext());

                    alertDialog.setTitle("Canceling");
                    alertDialog.setMessage("Are you sure?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            android.app.AlertDialog.Builder progressDialog = new AlertDialog.Builder(
                                    itemView.getContext());

                            getBindingAdapterPosition();
                            SharedPreferences sharedPreferences = itemView.getContext()
                                    .getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                            AlertDialog alertDialog = (AlertDialog) dialog;

                            Button yesButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            ButtonManager.disableButton(yesButton);

                            appointmentRepository.cancelAppointment(appointment.getDocumentId(),
                                    new AppointmentRepository.AppointmentCancelCallback() {
                                        @Override
                                        public void onSuccess(String appointmentId) {
                                            notificationRepository
                                                    .NotifyCancelledAppointmentToHealthProf(appointmentId);

                                            android.app.AlertDialog.Builder progressDialog = new AlertDialog.Builder(
                                                    itemView.getContext());

                                            progressDialog.setTitle("Canceled");
                                            progressDialog.setMessage("appointment was canceled");
                                            progressDialog.show();
                                        }

                                        @Override
                                        public void onError(String errorMessage) {
                                            ButtonManager.enableButton(yesButton);
                                        }
                                    });
                            appointmentRepository.addReport(appointment.getDocumentId(), ReportConstants.CANCELLED_APPOINTMENT,
                                    new AppointmentRepository.ReportCallback() {
                                        @Override
                                        public void onSuccess(String appointmentId) {
                                            Toast.makeText(itemView.getContext(), appointmentId + " updated",
                                                    Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onError(String errorMessage) {

                                        }
                                    });
                            myEdit.putInt("PatientPending", Integer.parseInt("10"));
                            myEdit.putInt("PatientStatus", Integer.parseInt("10"));
                            myEdit.apply();
                        }
                    });
                    alertDialog.show();

                }
            });
        }

        private void showUpdateDialog(AppointmentDto dto) {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.fragment_patient_appointment_reschedule);

            Button dateBtn = dialog.findViewById(R.id.dateBtn);
            TextView updateDate = dialog.findViewById(R.id.updateDate);
            TextView dateErrorText = dialog.findViewById(R.id.dateErrorText);
            TextView timeErrorText = dialog.findViewById(R.id.timeErrorText);

            Button timeBtn = dialog.findViewById(R.id.timeBtn);
            TextView updateTime = dialog.findViewById(R.id.updateTime);
            Button confirmBtn = dialog.findViewById(R.id.confirmbutton);
            DateTimeDto selectedDateTime = new DateTimeDto();

            String oldDate = date.getText().toString();
            String oldTime = time.getText().toString();
            String oldDateOldTime = date.getText().toString() + " " + time.getText().toString();
            DateTimeDto dateOfAppointment = DateTimeDto.ToDateTimeDto(dto.getDateOfAppointment());

            dateBtn.setOnClickListener((View.OnClickListener) v -> {
                // Get the current date
                DateDto date = dateOfAppointment.getDate();

                // Create and show the Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // Store the selected date
                            selectedDateTime.setDate(new DateDto(year1, monthOfYear + 1, dayOfMonth));

                            // Update the text on the button
                            updateDate.setText(selectedDateTime.getDate().ToString(false));
                        }, date.getYear(), date.getMonth(), date.getDay());

                // Show the Date Picker Dialog
                datePickerDialog.show();
                updateTime.setText("Time");
            });

            timeBtn.setOnClickListener(v -> {
                // Get the current time
//                TimeDto time = dateOfAppointment.getTime();
//
//                // Create and show the Time Picker Dialog
//                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
//                        (view, hourOfDay, minute1) -> {
//                            // Store the selected time
//
//                            selectedDateTime.setTime(new TimeDto(hourOfDay, minute1));
//
//                            // Update the text on the button
//                            updateTime.setText(selectedDateTime.getTime().ToString());
//                        }, time.getHour(), time.getMinute(), false);
//
//                // Show the Time Picker Dialog
//                timePickerDialog.show();
//                selectedDateTime.setDate(new DateDto(timepickyear.intValue(), timepickmonth.intValue(), timepickday.intValue()));
                if (updateDate.getText().toString().equals("Date") || updateDate.getText().toString().equals(" ")){

                    Toast.makeText(itemView.getContext(), "Error: must select date first", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    selectedDateTime.setTime(new TimeDto(6, 33));

                    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                    // TimeZone tz = TimeZone.getTimeZone("Asia/Singapore");
                    TimeZone tz = TimeZone.getDefault();
                    inFormat.setTimeZone(tz);
                    Date date = null;
                    try {
                        date = inFormat.parse(updateDate.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                    SimpleDateFormat outYear = new SimpleDateFormat("yyyy");
                    SimpleDateFormat outMonth = new SimpleDateFormat("MM");
                    SimpleDateFormat outDay = new SimpleDateFormat("dd");

                    String goal = outFormat.format(date);
                    String timepickYear = outYear.format(date);
                    String timepickMonth = outMonth.format(date);
                    String timepickDay = outDay.format(date);

                    appointmentRepository.checkAppointmentExists(goal, selectedDateTime.ToTimestampForTimePicker(), Integer.valueOf(timepickYear), Integer.valueOf(timepickMonth), Integer.valueOf(timepickDay), new AppointmentRepository.CheckAppointmentExistFetchCallback() {
                        public void onSuccess(ArrayList<String> lngList) {

                            //AnotherPickTimeSlot(lngList);
                            Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.time_slots_picker);
                            Button cancelBtn = dialog.findViewById(R.id.timePickCancel);

                            String Carl = "";

                            ListView hello = dialog.findViewById(R.id.breakdown);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(itemView.getContext(), android.R.layout.simple_list_item_1, lngList) {

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {

                                    TextView textView = (TextView) super.getView(position, convertView, parent);
                                    textView.setTextSize(15);
                                    return textView;
                                }
                            };

                            hello.setOnItemClickListener((adapterView, view, i, l) -> {
                                // String s = hello.getItemAtPosition(i).toString();
                                String itemValue = (String) hello.getItemAtPosition(i);
                                Toast.makeText(itemView.getContext(), itemValue, Toast.LENGTH_LONG).show();
                                if (!itemValue.equals("Not available")) {
                                    // updateTime.setText(itemValue);
                                    setTimePick(itemValue);
                                    updateTime.setText(itemValue);
                                    dialog.dismiss();
                                }
                                // adapter.dismiss(); // If you want to close the adapter
                            });
                            cancelBtn.setOnClickListener(v -> {
                                dialog.dismiss();
                            });

                            hello.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            // dialog.getWindow().setGravity(Gravity.LEFT);
                            //updateTime.setText(getTimePick());
                            dialog.show();
                           // updateTime.setText(getTimePick());
                        }

                        @Override
                        public void onError(String errorMessage) {


                        }

                    });
                    //updateTime.setText(getTimePick());
                    //FromDateandTime(updateDate.getText().toString(),timepickyear,timepickmonth,timepickday);
                }
            });

            confirmBtn.setOnClickListener(v -> {
                dateErrorText.setVisibility(View.GONE);
                timeErrorText.setVisibility(View.GONE);

                boolean inputInvalid = false;

                if (selectedDateTime.getDate() == null ||  selectedDateTime.getDate().getYear() == 0 || selectedDateTime.getDate().getMonth() == 0 || selectedDateTime.getDate().getDay() == 0) {
                    Toast.makeText(context, "Please select a valid date", Toast.LENGTH_SHORT).show();
                    dateErrorText.setVisibility(View.VISIBLE);
                    inputInvalid = true;
                }

                if (selectedDateTime.getTime() == null || AppointmentFunctions.IsValidHour(selectedDateTime.getTime())) {
                    Toast.makeText(context, "Please select a valid time", Toast.LENGTH_SHORT).show();
                    timeErrorText.setVisibility(View.VISIBLE);
                    inputInvalid = true;
                }

                if(inputInvalid)
                {
                    return;
                }

                String newDateNewTime = updateDate.getText().toString() + " " + updateTime.getText().toString();
                if (updateDate.getText().toString().equals("Date") || updateTime.getText().toString().equals("Time") || updateTime.getText().toString().equals(" ")) {
                    Toast.makeText(itemView.getContext(), "Error: must select date and time", Toast.LENGTH_SHORT)
                            .show();
                }else {
                    if (newDateNewTime.compareTo(oldDateOldTime) <= 0) {
                        Toast.makeText(itemView.getContext(), "Error: selected date and time must be higher",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("MySharedPref",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        ButtonManager.disableButton(confirmBtn);

                        notificationRepository.NotifyRescheduledAppointmentToPatient(dto.getDocumentId(),
                                DateTimeDto.ToDateTimeDto(selectedDateTime.ToTimestamp()),
                                new NotificationRepository.NotificationPushedCallback() {
                                    @Override
                                    public void onNotificationDone() {
                                        appointmentRepository.rescheduleAppointment(dto.getDocumentId(),
                                                selectedDateTime.ToTimestamp(),
                                                new AppointmentRepository.AppointmentRescheduleCallback() {

                                                    @Override
                                                    public void onSuccess(String appointmentId) {
                                                        Toast.makeText(itemView.getContext(),
                                                                appointmentId + " updated", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                    }

                                                    @Override
                                                    public void onError(String errorMessage) {
                                                        ButtonManager.enableButton(confirmBtn);
                                                    }
                                                });
                                    }
                                });

                        appointmentRepository.addReport(dto.getDocumentId(), ReportConstants.RESCHEDULED_APPOINTMENT,
                                new AppointmentRepository.ReportCallback() {
                                    @Override
                                    public void onSuccess(String appointmentId) {
                                        Toast.makeText(itemView.getContext(), appointmentId + " updated",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onError(String errorMessage) {

                                    }
                                });
                        myEdit.putInt("PatientPending", Integer.parseInt("10"));
                        myEdit.putInt("PatientStatus", Integer.parseInt("10"));
                        myEdit.apply();
                    }
                }
            });
            dialog.show();
        }
    }
}
