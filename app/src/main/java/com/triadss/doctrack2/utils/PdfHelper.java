package com.triadss.doctrack2.utils;

import static java.lang.Math.ceil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.healthprof.adapters.HealthProfessionalReportAdapter;
import com.triadss.doctrack2.config.constants.PdfConstants;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.ReportDto;
import com.triadss.doctrack2.types.Vector2i;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.function.Consumer;

public class PdfHelper {
    public static void GeneratePdfFromReports(Context context,
      String fileName,
      List<ReportDto> reportList, Consumer<File> afterCreate) {
        int pageWidth = PdfConstants.LETTER_PAGE_WIDTH;
        int pageHeight = PdfConstants.LETTER_PAGE_HEIGHT;

        PdfDocument pdf = new PdfDocument();

        /**Find total page required for the pdf**/
        int totalPage = getRequiredPages(reportList.size());
        int pageNum = 0;

        /**If the total item is less than pageCapacity then we take the item size as new capacity**/
        int finalPageCapacity = Math.min(PdfConstants.REPORTS_PER_PAGE, reportList.size());

        /**Configure Positioning in px**/
        int margin = 20;
        int topMargin = 40;
        int textSize = 12;
        //Width per report
        int width = pageWidth - (2 * margin); //2 for both sides
        //Label Distance
        int labelDistance = 100;
        //Vertical Distance before next Report
        int verticalDistance = 25;
        Vector2i currentPos = new Vector2i(margin, topMargin);

        /**Canvas Config**/
        Paint textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        textPaint.setColor(ContextCompat.getColor(context, R.color.black));

        Paint textPaintBold = new Paint();
        textPaintBold.setTextSize(textSize);
        textPaintBold.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaintBold.setColor(ContextCompat.getColor(context, R.color.black));

        /**We paginated the list and add them to parentView**/
        while (pageNum < totalPage) {
            List<ReportDto> currentPageItems = reportList.subList(
                    pageNum * finalPageCapacity,
                    Math.min(++pageNum * finalPageCapacity, reportList.size())
            );
            currentPos = new Vector2i(margin, topMargin);

            /**After adding items to view we render the new page and add it to pdf **/
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                    .Builder(pageWidth, pageHeight, pageNum)
                    .create();
            PdfDocument.Page dynamicPage = pdf.startPage(pageInfo);
            Canvas canvas = dynamicPage.getCanvas();

            for(ReportDto reportDto : currentPageItems) {
                /* Action */
                canvas.drawText(reportDto.getAction(), currentPos.getX(), currentPos.getY(), textPaintBold);
                currentPos = currentPos.withY(currentPos.getY() + textSize);

                int currentX = currentPos.getX();

                /* Date */
                DateTimeDto dateTime = DateTimeDto.ToDateTimeDto(reportDto.getCreatedDate());
                canvas.drawText("Date", currentPos.getX(), currentPos.getY(), textPaint);
                currentPos = currentPos.withX(currentPos.getX() + labelDistance);

                canvas.drawText(dateTime.formatDateTime(), currentPos.getX(), currentPos.getY(), textPaint);
                currentPos = new Vector2i(currentX,currentPos.getY() + textSize);

                /* Description */
                canvas.drawText("Description", currentPos.getX(), currentPos.getY(), textPaint);
                currentPos = currentPos.withX(currentPos.getX() + labelDistance);

                canvas.drawText(reportDto.getMessage(), currentPos.getX(), currentPos.getY(), textPaint);
                currentPos = new Vector2i(currentX,currentPos.getY() + verticalDistance);
            }

            pdf.finishPage(dynamicPage);
        }

        //Export
        // below line is used to set the name of
        // our PDF file and its path.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName + ".pdf");

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

    private static int getRequiredPages(int totalItems) {
        return (int)ceil((double) totalItems / PdfConstants.REPORTS_PER_PAGE);
    }
}
