package com.triadss.doctrack2.utils;

import static java.lang.Math.ceil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.PdfConstants;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.ReportDto;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Consumer;

public class PdfHelper {
    public static void GeneratePdfFromReports(Context context,
      List<ReportDto> reportList, Consumer<File> afterCreate) {
        int pageWidth = PdfConstants.LETTER_PAGE_WIDTH;
        int pageHeight = PdfConstants.LETTER_PAGE_HEIGHT;

        PdfDocument pdf = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                .Builder(pageWidth, pageHeight, 1)
                .create();

        LinearLayout parentView = PdfReportParentView(context);

        /**Find total page required for the pdf**/
        int totalPage = getRequiredPages(reportList.size());
        int pageNum = 0;

        /**If the total item is less than pageCapacity then we take the item size as new capacity**/
        int finalPageCapacity = Math.min(PdfConstants.REPORTS_PER_PAGE, reportList.size());

        /**We paginated the list and add them to parentView**/
//        while (pageNum < totalPage) {
//            List<ReportDto> currentPageItems = reportList.subList(
//                    pageNum * finalPageCapacity,
//                    Math.min(++pageNum * finalPageCapacity, reportList.size())
//            );
//
//            for(ReportDto reportDto : currentPageItems) {
//                parentView.addView(ReportListView(context, reportDto));
//            }
//
//            /**After adding items to view we render the new page and add it to pdf **/
//            PdfDocument.Page dynamicPage= pdf.startPage(pageInfo);
//            parentView.draw(dynamicPage.getCanvas());
//            //            loadBitmapFromView(parentView, dynamicPage.getCanvas(), pageWidth, pageHeight);
//            pdf.finishPage(dynamicPage);
//            parentView.removeAllViews();
//        }
        PdfDocument.Page dynamicPage= pdf.startPage(pageInfo);
        parentView.draw(dynamicPage.getCanvas());
        pdf.finishPage(dynamicPage);
        parentView.removeAllViews();
        //Export
        // below line is used to set the name of
        // our PDF file and its path.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "Test.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdf.writeTo(Files.newOutputStream(file.toPath()));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(context, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
            afterCreate.accept(file);
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdf.close();
    }

//    private static Bitmap loadBitmapFromView(View v, Canvas c, int width, int height) {
//        int specWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
//        int specHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
//        v.measure(specWidth, specHeight);
//        int requiredWidth = v.getMeasuredWidth();
//        int requiredHeight = v.getMeasuredHeight();
//        Bitmap b = Bitmap.createBitmap(requiredWidth, requiredHeight, Bitmap.Config.ARGB_8888);
//        c.drawColor(Color.WHITE);
//        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
//        v.draw(c);
//        return b;
//    }

    private static View ReportListView(Context context, ReportDto reportDto) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_reports, null);
        TextView action = view.findViewById(R.id.actionValue);
        TextView date = view.findViewById(R.id.dateValue);
        TextView description = view.findViewById(R.id.descriptionValue);

        description.setText(reportDto.getMessage());
        action.setText(reportDto.getAction());

        DateTimeDto dateTime = DateTimeDto.ToDateTimeDto(reportDto.getCreatedDate());
        date.setText(dateTime.formatDateTime());

        return view;
    }


    private static LinearLayout PdfReportParentView(Context context) {
        LinearLayout parentView = new LinearLayout(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        parentView.setLayoutParams(params);
        parentView.setOrientation(LinearLayout.VERTICAL);
        parentView.setBackgroundColor(Color.parseColor("#666677"));

        TextView test = new TextView(context);
        test.setText("Sample Val");
        test.setTextColor(Color.parseColor("#000000"));

        parentView.addView(test);
        return parentView;
    }

    private static int getRequiredPages(int totalItems) {
        return (int)ceil((double) totalItems / PdfConstants.REPORTS_PER_PAGE);
    }
}
