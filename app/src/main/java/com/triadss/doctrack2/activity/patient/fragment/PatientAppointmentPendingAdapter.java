package com.triadss.doctrack2.activity.patient.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.DateDto;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.TimeDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import com.triadss.doctrack2.repoositories.AppointmentRepository;

// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class PatientAppointmentPendingAdapter extends RecyclerView.Adapter<PatientAppointmentPendingAdapter.ViewHolder> {
    AppointmentRepository appointmentRepository;
    ArrayList<AppointmentDto> appointments;
    Context context;

    public String TimePick;

    // Constructor for initialization
    public PatientAppointmentPendingAdapter(Context context, ArrayList<AppointmentDto> appointments) {
        this.context = context;

        this.appointments = appointments;
    }

    public void setTimePick(String time){
        if(time.equals("8:00 am - 9:00 am")) {this.TimePick = "8:00";}
        if(time.equals("9:00 am - 10:00 am")) {this.TimePick = "9:00";}
        if(time.equals("10:00 am - 11:00 am")) {this.TimePick = "10:00";}
        if(time.equals("11:00 am - 12:00 pm")) {this.TimePick = "11:00";}
        if(time.equals("12:00 pm - 1:00 pm")) {this.TimePick = "12:00";}
        if(time.equals("1:00 pm - 2:00 pm")) {this.TimePick = "13:00";}
        if(time.equals("2:00 pm - 3:00 pm")) {this.TimePick = "14:00";}
        if(time.equals("3:00 pm - 4:00 pm")) {this.TimePick = "15:00";}
        if(time.equals("4:00 pm - 5:00 pm")) {this.TimePick = "16:00";}


    }
    public String getTimePick(){

        return TimePick;
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

        private TextView purpose,date,time, documentId, patientName;
        public ViewHolder(View view) {
            super(view);
            Button cancel, reschedule;
            purpose = (TextView) view.findViewById(R.id.purposetext);
            date = (TextView) view.findViewById(R.id.appointment_date);
            time = (TextView) view.findViewById(R.id.appointment_time);
            cancel=(Button)itemView.findViewById(R.id.cancel_button);
            reschedule=(Button)itemView.findViewById(R.id.reschedule_button);
            documentId = (TextView) view.findViewById(R.id.IDtext);
            patientName = view.findViewById(R.id.nametext);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(itemView.getContext(), purpose.getText(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(itemView.getContext(), documentId.getText(), Toast.LENGTH_SHORT).show();
                    android.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(itemView.getContext());


                    alertDialog.setTitle("Canceling");
                    alertDialog.setMessage("Are you sure?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            android.app.AlertDialog.Builder progressDialog = new AlertDialog.Builder(itemView.getContext());

                            getBindingAdapterPosition();
                            SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                            appointmentRepository.cancelAppointment(documentId.getText().toString(), new AppointmentRepository.AppointmentCancelCallback() {
                                @Override
                                public void onSuccess(String appointmentId) {

                                    android.app.AlertDialog.Builder progressDialog = new AlertDialog.Builder(itemView.getContext());

                                    progressDialog.setTitle("Canceled");
                                    progressDialog.setMessage("appointment was canceled");
                                    progressDialog.show();
                                }
                                @Override
                                public void onError(String errorMessage) {

                                }
                            });
                            appointmentRepository.addReport(documentId.getText().toString(),"CANCEL", new AppointmentRepository.ReportCallback() {
                                @Override
                                public void onSuccess(String appointmentId) {
                                    Toast.makeText(itemView.getContext(), appointmentId + " updated", Toast.LENGTH_SHORT).show();

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
            reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), purpose.getText(), Toast.LENGTH_SHORT).show();
                    showUpdateDialog(documentId.getText().toString());
                    System.out.println();
                }
            });
        }

        public void update(AppointmentDto appointment)
        {
            purpose.setText(appointment.getPurpose());
            DateTimeDto dateTimeDto = DateTimeDto.ToDateTimeDto(appointment.getDateOfAppointment());
            date.setText(dateTimeDto.getDate().ToString());
            time.setText(dateTimeDto.getTime().ToString());
            documentId.setText(appointment.getDocumentId());
            patientName.setText(appointment.getNameOfRequester());
        }

        private void showUpdateDialog(String id)
        {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.fragment_patient_appointment_reschedule);

            Button dateBtn = dialog.findViewById(R.id.dateBtn);
            TextView updateDate = dialog.findViewById(R.id.updateDate);

            Button timeBtn = dialog.findViewById(R.id.timeBtn);
            timeBtn.setVisibility(View.GONE);
            TextView updateTime = dialog.findViewById(R.id.updateTime);
            Button confirmBtn = dialog.findViewById(R.id.confirmbutton);
            DateTimeDto selectedDateTime = new DateTimeDto();

            String oldDate = date.getText().toString();
            String oldTime = time.getText().toString();
            String oldDateOldTime = date.getText().toString()+ " " +time.getText().toString();

            AtomicInteger timepickyear = new AtomicInteger();
            AtomicInteger timepickmonth = new AtomicInteger();
            AtomicInteger timepickday = new AtomicInteger();
            dateBtn.setOnClickListener((View.OnClickListener) v -> {
                // Get the current date
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Create and show the Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // Store the selected date
                            selectedDateTime.setDate(new DateDto(year1, monthOfYear, dayOfMonth));
                            timeBtn.setVisibility(View.VISIBLE);
                            // Update the text on the button
                            updateDate.setText(selectedDateTime.getDate().ToString());

                           timepickyear.set(year1);
                           timepickmonth.set(monthOfYear);
                           timepickday.set(dayOfMonth);
                        }, year, month, day);

                // Show the Date Picker Dialog
                datePickerDialog.show();
            });

//            timeBtn.setOnClickListener(v -> {
//
//                selectedDateTime.setDate(new DateDto(timepickyear.intValue(), timepickmonth.intValue(), timepickday.intValue()));
//                selectedDateTime.setTime(new TimeDto(12, 15));
//
//                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
//                // TimeZone tz = TimeZone.getTimeZone("Asia/Singapore");
//                TimeZone tz = TimeZone.getDefault();
//                inFormat.setTimeZone(tz);
//                Date date = null;
//                try {
//                    date = inFormat.parse(updateDate.getText().toString());
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//
//                SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
//                String goal = outFormat.format(date);
//
//                appointmentRepository.checkAppointmentExists(goal,selectedDateTime.ReschedToTimestamp(),timepickyear.intValue(),timepickmonth.intValue(),timepickday.intValue(),new AppointmentRepository.CheckAppointmentExistFetchCallback() {
//                    public void onSuccess(ArrayList<String> lngList) {
//
//                        AnotherPickTimeSlot(lngList);
//                        //updateTime.setText(getTimePick());
//                        //setTimePick(" ");
//                        //updateTime.setText(getTimePick());
//                    }
//
//                    @Override
//                    public void onError(String errorMessage) {
//
//
//                    }
//                });
//                updateTime.setText(getTimePick());
//                //FromDateandTime(updateDate.getText().toString(),timepickyear,timepickmonth,timepickday);
//            });

            timeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedDateTime.setDate(new DateDto(timepickyear.intValue(), timepickmonth.intValue(), timepickday.intValue()));
                    selectedDateTime.setTime(new TimeDto(12, 15));

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
                    String goal = outFormat.format(date);

                    appointmentRepository.checkAppointmentExists(goal,selectedDateTime.ReschedToTimestamp(),timepickyear.intValue(),timepickmonth.intValue(),timepickday.intValue(),new AppointmentRepository.CheckAppointmentExistFetchCallback() {
                        public void onSuccess(ArrayList<String> lngList) {

                            //AnotherPickTimeSlot(lngList);
                            Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.time_slots_picker);
                            Button cancelBtn = dialog.findViewById(R.id.timePickCancel);

                            String Carl = "";

                            ListView hello = dialog.findViewById(R.id.breakdown);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(itemView.getContext(),android.R.layout.simple_list_item_1,lngList){

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {

                                    TextView textView = (TextView) super.getView(position, convertView, parent);
                                    textView.setTextSize(15);
                                    return textView;
                                }
                            };

                            hello.setOnItemClickListener((adapterView, view, i, l) -> {
                                // String s = hello.getItemAtPosition(i).toString();
                                String  itemValue = (String) hello.getItemAtPosition(i);
                                Toast.makeText(itemView.getContext(), itemValue, Toast.LENGTH_LONG).show();
                                if(!itemValue.equals("Occupied")){
                                    // updateTime.setText(itemValue);

                                    setTimePick(itemValue);
                                    updateTime.setText(getTimePick());
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
                            updateTime.setText(getTimePick());
                            dialog.show();
                            updateTime.setText(getTimePick());
                        }

                        @Override
                        public void onError(String errorMessage) {


                        }

                    });
                    updateTime.setText(getTimePick());
                    //FromDateandTime(updateDate.getText().toString(),timepickyear,timepickmonth,timepickday);
                }
            });
            confirmBtn.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), getTimePick(), Toast.LENGTH_LONG).show();

//                String newDateNewTime = updateDate.getText().toString()+ " " +updateTime.getText().toString();
//                if(updateDate.getText().toString().equals("Date") || updateTime.getText().toString().equals("Time")){
//                    Toast.makeText(itemView.getContext(), "Error: must select date and time", Toast.LENGTH_SHORT).show();
//                }else{
//                    if (newDateNewTime.compareTo(oldDateOldTime) <= 0){
//                        Toast.makeText(itemView.getContext(), "Error: selected date and time must be higher", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
//                      SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                      appointmentRepository.rescheduleAppointment(id, selectedDateTime.ReschedToTimestamp(), new AppointmentRepository.AppointmentRescheduleCallback() {
//                      @Override
//                      public void onSuccess(String appointmentId) {
//                        Toast.makeText(itemView.getContext(), appointmentId + " updated", Toast.LENGTH_SHORT).show();
//
//                       }
//                      @Override
//                      public void onError(String errorMessage) {
//
//                      }
//                      });
//                      appointmentRepository.addReport(id,"RESCHEDULE", new AppointmentRepository.ReportCallback() {
//                      @Override
//                      public void onSuccess(String appointmentId) {
//                        Toast.makeText(itemView.getContext(), appointmentId + " updated", Toast.LENGTH_SHORT).show();
//
//                      }
//                      @Override
//                      public void onError(String errorMessage) {
//
//                      }
//                  });
//                    myEdit.putInt("PatientPending", Integer.parseInt("10"));
//                    myEdit.putInt("PatientStatus", Integer.parseInt("10"));
//                    myEdit.apply();
//
//                    }
//                }

//                SharedPreferences sharedPreferences = itemView.getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
//                SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                appointmentRepository.rescheduleAppointment(id, selectedDateTime.ToTimestamp(), new AppointmentRepository.AppointmentRescheduleCallback() {
//                    @Override
//                    public void onSuccess(String appointmentId) {
//                        Toast.makeText(itemView.getContext(), appointmentId + " updated", Toast.LENGTH_SHORT).show();
//
//                    }
//                    @Override
//                    public void onError(String errorMessage) {
//
//                    }
//                });
//                appointmentRepository.addReport(id,"RESCHEDULE", new AppointmentRepository.ReportCallback() {
//                    @Override
//                    public void onSuccess(String appointmentId) {
//                        Toast.makeText(itemView.getContext(), appointmentId + " updated", Toast.LENGTH_SHORT).show();
//
//                    }
//                    @Override
//                    public void onError(String errorMessage) {
//
//                    }
//                });
//                myEdit.putInt("PatientPending", Integer.parseInt("10"));
//                myEdit.putInt("PatientStatus", Integer.parseInt("10"));
//                myEdit.apply();


//                 String newDateNewTime = updateDate.getText().toString()+ " " +updateTime.getText().toString();
//                 Log.d("dateANDtime",oldDateOldTime);
//                 Log.d("dateANDtime", newDateNewTime);
//
//
//                Log.d("result",String.valueOf(oldDateOldTime.compareTo(newDateNewTime)));
//                //Toast.makeText(itemView.getContext(), newDateNewTime.compareTo(oldDateOldTime), Toast.LENGTH_SHORT).show();
//                Log.d("result2",String.valueOf("2024-03-24 11:38".compareTo("2024-04-19 15:56")));


            });
            dialog.show();
        }



        public void FromDateandTime(String SelectedDate,AtomicInteger year,AtomicInteger month,AtomicInteger day){


            DateTimeDto selectedDateTime = new DateTimeDto();
            selectedDateTime.setDate(new DateDto(year.intValue(), month.intValue(), day.intValue()));
            selectedDateTime.setTime(new TimeDto(12, 15));

            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            // TimeZone tz = TimeZone.getTimeZone("Asia/Singapore");
            TimeZone tz = TimeZone.getDefault();
            inFormat.setTimeZone(tz);
            Date date = null;
            try {
                date = inFormat.parse(SelectedDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String goal = outFormat.format(date);

            appointmentRepository.checkAppointmentExists(goal,selectedDateTime.ReschedToTimestamp(),year.intValue(),month.intValue(),day.intValue(),new AppointmentRepository.CheckAppointmentExistFetchCallback() {
                public void onSuccess(ArrayList<String> lngList) {

                    AnotherPickTimeSlot(lngList);
                }

                @Override
                public void onError(String errorMessage) {


                }
            });

        }

        public void AnotherPickTimeSlot(ArrayList<String> lngList) { //added by Rudge 26/03/2024
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.time_slots_picker);
            Button cancelBtn = dialog.findViewById(R.id.timePickCancel);

            Dialog otherdialog = new Dialog(context);
            otherdialog.setContentView(R.layout.fragment_patient_appointment_reschedule);
            TextView updateTime = otherdialog.findViewById(R.id.updateTime);
            String Carl = "";

            ListView hello = dialog.findViewById(R.id.breakdown);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(itemView.getContext(),android.R.layout.simple_list_item_1,lngList){

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    TextView textView = (TextView) super.getView(position, convertView, parent);
                    textView.setTextSize(15);
                    return textView;
                }
            };

            hello.setOnItemClickListener((adapterView, view, i, l) -> {
                // String s = hello.getItemAtPosition(i).toString();
                String  itemValue = (String) hello.getItemAtPosition(i);
                Toast.makeText(itemView.getContext(), itemValue, Toast.LENGTH_LONG).show();
                if(!itemValue.equals("Occupied")){
                   // updateTime.setText(itemValue);

                    setTimePick(itemValue);
                    updateTime.setText(getTimePick());
                    dialog.dismiss();
                    dialog.show();
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
            dialog.show();

            //return getTimePick();
        }

    }
}
