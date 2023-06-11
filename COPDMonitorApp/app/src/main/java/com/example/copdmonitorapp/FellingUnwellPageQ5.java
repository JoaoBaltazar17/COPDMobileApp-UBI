package com.example.copdmonitorapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FellingUnwellPageQ5 extends AppCompatActivity {


        // Navigation Drawer Attributes
        DrawerLayout drawerLayout;
        ImageView menu;
        LinearLayout home, settings, share, about, logout;


        // Components

        TextView txtViewImageVector;
        TextView txtViewTestResult;
        TextView txtViewInstruction1;
        TextView txtViewInstruction2;
        TextView txtViewInstruction3;
        TextView txtViewInstruction4;
        Button btnShareResultsQuestionnaire;
        Drawable drawable;


        // Slide Values
        int slide3_value;
        int slide2_value;
        int slide1_value;
        int slide4_value;

        int sumSlides;


        String pacientLoggedEmail;
        String pacientLoggedName;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fellingunwell_pageq5);

            // Retrieve user's login credentials
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            pacientLoggedEmail = sharedPreferences.getString("email", "");
            pacientLoggedName = sharedPreferences.getString("name", "");


            // Component Finder's
            txtViewImageVector = findViewById(R.id.txtViewImageVector);
            txtViewTestResult = findViewById(R.id.txtViewtTestResult);;
            txtViewInstruction1 = findViewById(R.id.textViewInst1);
            txtViewInstruction2 = findViewById(R.id.textViewInst2);
            txtViewInstruction3 = findViewById(R.id.textViewInst3);
            txtViewInstruction4= findViewById(R.id.textViewInst4);
            btnShareResultsQuestionnaire = findViewById(R.id.btnShareResultsQuestion);

            // Receiving values of question's
            Intent intent = getIntent();
            slide1_value = intent.getIntExtra("slide_value1", 0);
            slide2_value = intent.getIntExtra("slide_value2", 0);
            slide3_value = intent.getIntExtra("slide_value3", 0);
            slide4_value = intent.getIntExtra("slide_value4", 0);

            // Display percentage, drawable and instructions depending on information:
            sumSlides = slide1_value + slide2_value + slide3_value + slide4_value;
            Log.e("VALUE SLIDES", "SLIDE 1: " + slide1_value + "\nSLIDE 2: " + slide2_value + "\nSLIDE 3: " + slide3_value + "\nSLIDE 4: " + slide4_value + "\nSUM: " + sumSlides);


            if (sumSlides >= 0 && sumSlides <= 3) {
                drawable = getResources().getDrawable(R.drawable.ic_questionhappy);
                txtViewImageVector.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                txtViewTestResult.setText("Low Impact");
                txtViewInstruction1.setText(getString(R.string.lowimpact_inst1));
                txtViewInstruction2.setText(getString(R.string.lowimpact_inst2));
                txtViewInstruction3.setText(getString(R.string.lowimpact_inst3));
                txtViewInstruction4.setText(getString(R.string.lowimpact_inst4));

            } else if (sumSlides >= 4 && sumSlides <= 7) {
                drawable = getResources().getDrawable(R.drawable.ic_questionmoderate);
                txtViewImageVector.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                txtViewTestResult.setText("Medium Impact");
                txtViewInstruction1.setText(getString(R.string.mediumimpact_inst1));
                txtViewInstruction2.setText(getString(R.string.mediumimpact_inst2));
                txtViewInstruction3.setText(getString(R.string.mediumimpact_inst3));
                txtViewInstruction4.setText(getString(R.string.mediumimpact_inst4));

            } else if (sumSlides >= 8 && sumSlides <= 11) {
                drawable = getResources().getDrawable(R.drawable.ic_questionsad);
                txtViewImageVector.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                txtViewTestResult.setText("High Impact");
                txtViewInstruction1.setText(getString(R.string.high_inst1));
                txtViewInstruction2.setText(getString(R.string.high_inst2));
                txtViewInstruction3.setText(getString(R.string.high_inst3));
                txtViewInstruction4.setText(getString(R.string.high_inst4));

            } else if (sumSlides >= 12 && sumSlides <= 16) {
                drawable = getResources().getDrawable(R.drawable.ic_questionverysad);
                txtViewImageVector.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                txtViewTestResult.setText("Very High Impact");
                txtViewInstruction1.setText(getString(R.string.veryhigh_inst1));
                txtViewInstruction2.setText(getString(R.string.veryhigh_inst2));
                txtViewInstruction3.setText(getString(R.string.veryhigh_inst3));
                txtViewInstruction4.setText(getString(R.string.veryhigh_inst4));

            } else {
                Log.e("Resultados", "Pontuação Inválida: " + sumSlides);
            }

            // Navigation Drawer Finders
            drawerLayout = findViewById(R.id.drawerLayout);
            menu = findViewById(R.id.menu);
            home = findViewById(R.id.home);
            about = findViewById(R.id.about);
            logout = findViewById(R.id.logout);
            settings = findViewById(R.id.settings);
            share = findViewById(R.id.share);


            // Menu Navigation and Components Listener's
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDrawer(drawerLayout);
                }
            });

            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity( FellingUnwellPageQ5.this, HomePage.class);
                }
            });
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity( FellingUnwellPageQ5.this, SettingsPage.class);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity( FellingUnwellPageQ5.this, SharePage.class);
                }
            });
            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectActivity( FellingUnwellPageQ5.this, AboutPage.class);
                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("username");
                    editor.remove("password");
                    editor.apply();

                    Intent intent = new Intent(FellingUnwellPageQ5.this, InitialPage.class);
                    startActivity(intent);
                }
            });


        }

        // Navigation Drawer Methods
        public static void openDrawer(DrawerLayout drawerLayout) {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        public static void closeDrawer(DrawerLayout drawerLayout) {
            if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        }

        public static void redirectActivity(Activity activity, Class secondActivity) {
            Intent intent = new Intent(activity, secondActivity);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            activity.finish();
        }



        @Override
        protected void onPause() {
            super.onPause();
            closeDrawer(drawerLayout);
        }


    public void sendDetailedReportQuestionnaire(View view) {

        ArrayList<String> contentPDF = new ArrayList<>();

        String sosDate = getCurrentDateString();
        contentPDF.add(sosDate);

        // Generate PDF file with the given string
        String pacientemailpdf = "Pacient Email: " + pacientLoggedEmail;
        contentPDF.add(pacientemailpdf);

        String pacientnamepdf = "Pacient Name: " + pacientLoggedName;
        contentPDF.add(pacientnamepdf);

        String question1 = "Question 1: " + getString(R.string.question1);
        contentPDF.add(question1);
        String answer1 = "Patient Answer: " + slide1_value;
        contentPDF.add(answer1);

        String question2 = "Question 2: " + getString(R.string.question2);
        contentPDF.add(question2);
        String answer2 = "Patient Answer: " + slide2_value;
        contentPDF.add(answer2);

        String question3 = "Question 3: " + getString(R.string.question3);
        contentPDF.add(question3);
        String answer3 = "Patient Answer: " + slide3_value;
        contentPDF.add(answer3);

        String question4 = "Question 4: " + getString(R.string.question4);
        contentPDF.add(question4);
        String answer4 = "Patient Answer: " + slide4_value;
        contentPDF.add(answer4);

        String finalresult = "Final Result: " + sumSlides + "  {0 - 16}";
        contentPDF.add(finalresult);

        String description = "This test has made in COPD Monitor App!";
        contentPDF.add(description);


        File pdfFile = generatePDF(contentPDF);

        sendFileViaIntent(pdfFile, "application/pdf", "PDF File - Report SOS ");
    }

    private File generatePDF(ArrayList<String> content) {
        File pdfFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "my_pdf_file.pdf");

        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            for (String string : content) {
                Paragraph p = new Paragraph(string);
                // Adjust the spacing before and after each paragraph
                p.setMarginTop(10);
                p.setMarginBottom(10);
                document.add(p);

            }
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pdfFile;
    }

    private void sendFileViaIntent(File file, String mimeType, String title) {
        Uri fileUri = FileProvider.getUriForFile(this, "com.example.copdmonitorapp", file);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Add this line

        startActivity(Intent.createChooser(intent, "Share file via..."));
    }


    // Date

    private String getCurrentDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        return currentDate;
    }



}