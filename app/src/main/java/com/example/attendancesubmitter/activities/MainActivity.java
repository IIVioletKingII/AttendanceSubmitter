package com.example.attendancesubmitter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.attendancesubmitter.R;
import com.example.attendancesubmitter.databinding.ActivityMainBinding;
import com.example.attendancesubmitter.utilities.DatabaseHelper;
import com.example.attendancesubmitter.utilities.FormUtils;
import com.example.attendancesubmitter.utilities.Person;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

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

//		DatabaseHelper databaseHelper = new DatabaseHelper( this );


		recyclerView = binding.recyclerView;
		setRecyclerView( );
	}


	private void setRecyclerView( ) {

		recyclerView.setAdapter( new PersonAdapter( getPersonNames( )/*new String[]{ "Sam", "Corban", "Dylan" }*/ ) );
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

	public void addPerson( View view ) {

		createAddPersonList( this );
	}

	private void createAddPersonList( Context context ) {

		AlertDialog.Builder builder = new AlertDialog.Builder( context );
		builder.setTitle( "Enter Contract Id" );
		// Set an EditText view to get user input
		final EditText input1 = new EditText( context );
		final EditText input2 = new EditText( context );
		final EditText input3 = new EditText( context );
//		final GridView gridView = new GridView( context );
//		gridView.addView( input1 );
//		gridView.addView( input2 );
//		gridView.addView( input3 );
		//input. // <-- wanted to try and change the underline width of the  input
		builder.setView( input1 );

		builder.setPositiveButton( "Request", ( dialog, whichButton ) -> {

			String result = input1.getText( ).toString( );

//			new Thread( ( ) -> downloadContract( result ) ).start( );
//
//			createContractOptions( context );
		} );

		builder.setNegativeButton( "Back", ( dialog, whichButton ) -> {
			// Canceled, exit
		} );
		builder.show( );
	}


	private List<Person> getPersons( ) {

		DatabaseHelper databaseHelper = new DatabaseHelper( this );
		List<Person> persons = databaseHelper.getPersons( );
		databaseHelper.close( );
		return persons;
	}


	private List<String> getPersonNames( ) {

		DatabaseHelper databaseHelper = new DatabaseHelper( this );
		List<String> persons = databaseHelper.getNames( );
		databaseHelper.close( );
		return persons;
	}

	public void addPerson( Person person ) {
		DatabaseHelper databaseHelper = new DatabaseHelper( this );
		databaseHelper.addData( person );
		databaseHelper.close( );
	}

}