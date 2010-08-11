package nemec.GPSHeartbeat;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.SeekBar;

public class DefaultActivity extends Activity {
    
	private Intent svc;
	
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
        
        /*intervals = new TreeMap<Integer, Integer>();
        intervals.put(10, 	 0);
        intervals.put(30,	 15);
        intervals.put(1*60,	 30);
        intervals.put(2*60,	 50);
        intervals.put(6*60,	 60);
        intervals.put(12*60, 80);
        intervals.put(24*60, 100);*/
        
                
    }
    
}
