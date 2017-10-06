package edu.gvsu.cis.sprowlsc.geocalculator;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static int unitSelectorDistance = 0;
    public static int unitSelectorBearing = 0;
    public static double[] distanceUnits = new double[2];
    public static double[] bearingUnits = new double[2];
    public static String[] unitNamesDistance = new String[2];
    public static String[] unitNamesBearing = new String[2];
    public static double radius = 1000;
    public static double degreeMils = 1;

    //Buttons
    Button calcButton;
    Button clearButton;

    // Edit Texts
    EditText p1Lat;
    EditText p1Long;
    EditText p2Lat;
    EditText p2Long;

    // Text Views
    TextView distanceViewResult;
    TextView bearingViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        distanceUnits[1] = 1609.34;
        distanceUnits[0] = 1000.0;

        bearingUnits[1] = 17.777778;
        bearingUnits[0] = 1;

        unitNamesDistance[1] = "Miles";
        unitNamesDistance[0] = "Kilometers";

        unitNamesBearing[1] = "Mils";
        unitNamesBearing[0] = "Degrees";

        // Set up edit texts
        p1Lat = findViewById(R.id.p1Lat);
        p1Long = findViewById(R.id.p1Long);
        p2Lat = findViewById(R.id.p2Lat);
        p2Long = findViewById(R.id.p2Long);

        // Set up text fields
        distanceViewResult = findViewById(R.id.disViewResult);
        bearingViewResult = findViewById(R.id.bearingViewResult);

        // Set up buttons
        calcButton = findViewById(R.id.calcButton);
        calcButton.setOnClickListener(e -> updateValues());

        clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(e -> clearValues());

    }

    public void updateValues(){

        //if(p1Lat==0.0)
        double[] result = calculate();

        distanceViewResult.setText(result[0] + " " + unitNamesDistance[unitSelectorDistance]);
        bearingViewResult.setText(result[1] + " " + unitNamesBearing[unitSelectorBearing]);

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void clearValues(){
        p1Lat.setText("");
        p1Long.setText("");
        p2Lat.setText("");
        p2Long.setText("");

        distanceViewResult.setText("");
        bearingViewResult.setText("");
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public double[] calculate(){
        // Calc the distance between the chords update the other fields
        double[] result = new double[2];
        try {
            Location location1 = new Location("");
            location1.setLatitude(Double.parseDouble(p1Lat.getText().toString()));
            location1.setLongitude(Double.parseDouble(p1Long.getText().toString()));

            Location location2 = new Location("");
            location2.setLatitude(Double.parseDouble(p2Lat.getText().toString()));
            location2.setLongitude(Double.parseDouble(p2Long.getText().toString()));

            result[0] = (double) Math.round((location1.distanceTo(location2)/ radius)*100)/100;
            result[1] = (double) Math.round((location1.bearingTo(location2)*degreeMils)*100)/100;
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsScreen.class);
            intent.putExtra("distanceSelection", unitSelectorDistance);
            intent.putExtra("bearingSelection", unitSelectorBearing);
            try {
                startActivityForResult(intent, 0);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK) {
            radius = distanceUnits[data.getIntExtra("distanceSelection", 0)];
            System.out.println(data.getIntExtra("distanceSelection", 0));
            degreeMils = bearingUnits[data.getIntExtra("bearingSelection", 0)];

            unitSelectorDistance = data.getIntExtra("distanceSelection", 0);
            unitSelectorBearing = data.getIntExtra("bearingSelection", 0);
            // Update values with new units
            updateValues();
        }
    }
}
