package nemec.GPSHeartbeat;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class DefaultActivity extends Activity{
    
	private Intent svc;
	private ArrayList<String> valS;
	private ArrayList<Integer> valI;
	private final Integer DEFAULT_INTERVAL=60;
	private SharedPreferences.Editor prefsEditor;
	
	private OnClickListener onListener = new OnClickListener(){
		public void onClick(View v){
			startService(svc);
		}
	};

	private OnClickListener offListener = new OnClickListener(){
		public void onClick(View v){
			stopService(svc);
		}
	};
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        svc = new Intent(this, HeartbeatService.class);
        
        final RadioButton svcOn = (RadioButton) findViewById(R.id.RadioButtonOn);
        final RadioButton svcOff = (RadioButton) findViewById(R.id.RadioButtonOff);
        svcOn.setOnClickListener(onListener);
        svcOff.setOnClickListener(offListener);
        
    	String prefs_file = this.getString(R.string.prefs_file);
    	SharedPreferences prefs = getSharedPreferences(prefs_file, 0);
    	prefsEditor = prefs.edit();
        
        // Intervals are calculated in minutes.
        valS = new ArrayList<String>();
        valI = new ArrayList<Integer>();
        valS.add("10m");valI.add(10);
		valS.add("30m");valI.add(30);
		valS.add("1h");valI.add(60);
		valS.add("2h");valI.add(120);
		valS.add("6h");valI.add(360);
		valS.add("12h");valI.add(720);
		valS.add("24h");valI.add(1440);
        Spinner spin=(Spinner)findViewById(R.id.SpinnerInterval);
        
		ArrayAdapter<String> aa=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valS);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        
        spin.setSelection(valI.indexOf(getInterval()));
        
        spin.setOnItemSelectedListener( new Spinner.OnItemSelectedListener(){
        	public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
        		setInterval(valI.get(position));
        	}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
        });
    }
    
    private void setInterval(int val){
    	String interval_key = this.getString(R.string.interval_key);
    	prefsEditor.putInt(interval_key, val);
    	prefsEditor.commit();
    }
    
    private Integer getInterval(){
    	String prefs_file = this.getString(R.string.prefs_file);
    	String interval_key = this.getString(R.string.interval_key);
    	SharedPreferences settings = getSharedPreferences(prefs_file, 0);
    	return settings.getInt(interval_key, DEFAULT_INTERVAL);
    }
    
}
