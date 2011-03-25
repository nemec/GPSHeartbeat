package nemec.GPSHeartbeat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class HeartbeatService extends Service implements LocationListener, OnSharedPreferenceChangeListener{
	
	private int INTERVAL; // Interval is in seconds
	private final IBinder mBinder = new LocalBinder();
	private LocationManager locationManager;
	private String server_ip;
	private int server_port;
	
	public HeartbeatService(){

	}

	public void onCreate() {
		super.onCreate();
		startservice();
	}
	
	public void onDestroy(){
		super.onDestroy();
		stopservice();
	}
	
	private void startservice() {
		registerBootHook();
		
    	SharedPreferences prefs = getSharedPreferences(this.getString(R.string.prefs_file), 0);
    	INTERVAL = prefs.getInt(this.getString(R.string.interval_key),
    					60);
    	server_ip = prefs.getString(this.getString(R.string.server_ip_key),
    					"192.168.1.128");
    	server_port = prefs.getInt(this.getString(R.string.server_port_key),
    					9000);
    	
    	prefs.registerOnSharedPreferenceChangeListener(this);
    	
    	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, true);

        locationManager.requestLocationUpdates(provider, 10000, 0, this);
        
        Toast.makeText(getApplicationContext(), 
				"Starting service.", Toast.LENGTH_SHORT).show();
	}
	
		private void registerBootHook(){
			//TODO Start service at boot
		}
	
	private void stopservice() {
		unregisterBootHook();
		
		locationManager.removeUpdates(this);
		
		Toast.makeText(getApplicationContext(), 
				"Stopping service.", Toast.LENGTH_SHORT).show();
	}
	
		private void unregisterBootHook(){
			//TODO Stop service from starting at boot. 
		}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}
	
	public class LocalBinder extends Binder{
		HeartbeatService getService(){
			return HeartbeatService.this;
		}
		
	}
	
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key){
		if(key == HeartbeatService.this.getString(R.string.interval_key)){
			int newInterval = prefs.getInt(key, -1);
			if(newInterval > 0){
				INTERVAL = newInterval;
			}
			Log.v("PrefsChanged", "Interval has been changed: "+ newInterval);
		}
				
	}

	@Override
	public void onLocationChanged(Location l) {
		String data = "(" + l.getLatitude() + ", " + l.getLongitude() + ")";
		Log.v("Location", data);
		try {
			Socket connection = new Socket(server_ip, 9000);
			PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
			out.println(data);
			connection.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
