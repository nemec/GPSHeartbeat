package nemec.GPSHeartbeat;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class HeartbeatService extends Service implements LocationListener{
	
	private Timer timer;
	private int INTERVAL; // Interval is in minutes
	private final IBinder mBinder = new LocalBinder();
	
	public HeartbeatService(){
		this(60);
	}
	
	public HeartbeatService(int interval){
		timer = new Timer();
		INTERVAL=interval;
	}
	
	public void setInterval(int i){
		INTERVAL = i;
	}
	
	private Location getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, true);

        // In order to make sure the device is getting location, request
        // updates. locationManager.requestLocationUpdates(provider, 1, 0,
        // this);
        //locationManager.requestLocationUpdates(provider, 1, 0, this);
        return locationManager.getLastKnownLocation(provider);
    }

	
	private void postLocation(){
		Location l = getLocation();
		System.out.println(l);
	}

	public void onCreate() {
		super.onCreate();
		//startservice();
		Toast.makeText(getApplicationContext(), 
				"Starting service.", Toast.LENGTH_SHORT).show();


	}
	
	public void onDestroy(){
		super.onDestroy();
		//stopservice();

		Toast.makeText(getApplicationContext(), 
				"Stopping service.", Toast.LENGTH_SHORT).show();
	}
	
	private void startservice() {
		registerBootHook();
		
		timer.scheduleAtFixedRate( new TimerTask() {
			public void run() {	
				//postLocation();
			}
		}, 0, INTERVAL*60*1000);
	}
	
	private void registerBootHook(){
		//TODO Start service at boot
	}
	
	private void stopservice() {
		unregisterBootHook();
		if (timer != null){
			timer.cancel();
		}
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

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
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
