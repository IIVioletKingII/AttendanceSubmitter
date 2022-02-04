package com.example.attendancesubmitter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.attendancesubmitter.R;
import com.example.attendancesubmitter.databinding.ActivityMainBinding;
import com.example.attendancesubmitter.utilities.FormUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

	public static String formID = "1FAIpQLScahJirT2sVrm0qDveeuiO1oZBJ5B7J0gdeI7UAZGohKEmi9g";
	public static String clubID = "Robotics";

	private RecyclerView recyclerView;

	private static final int REQUEST_INTERNET = 2;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		ActivityMainBinding binding = ActivityMainBinding.inflate( getLayoutInflater( ) );
		setContentView( binding.getRoot( ) );

		recyclerView = binding.recyclerView;
		setRecyclerView( );
	}


	private void setRecyclerView( ) {

		recyclerView.setAdapter( new PersonAdapter( new String[]{ "Sam", "Corban", "Dylan" } ) );
	}

	public void takeAttendance( View view ) {
		Toast.makeText( this, "TAKING ATTENDANCE", Toast.LENGTH_SHORT ).show( );
		URL url = null;
		try {
			url = FormUtils.getResponseURL( formID, Calendar.getInstance( ), "DePoule", "Sam", 121875, clubID );
		} catch( MalformedURLException e ) {
			e.printStackTrace( );
		}
		Toast.makeText( this, "" + url, Toast.LENGTH_SHORT ).show( );
	}

	public void enterSettings( View view ) {
		Toast.makeText( this, "Settings", Toast.LENGTH_SHORT ).show( );

	}

	public void editLocation( String address ) {

//		Intent intent = new Intent( this, EditPersonActivity.class );
//		intent.putExtra( "CONTRACT_ID", contractName ).putExtra( "LOCATION_ADDRESS", address );
//		startActivity( intent );
	}

}