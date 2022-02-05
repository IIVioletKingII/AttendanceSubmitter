package com.example.attendancesubmitter.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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

	public static String FORM_ID = "1FAIpQLSfoLJU0ON3XtKrypUV7VmINCadxzyCqrnUroTZrUdYvG2QfJQ"; // original
	//	public static String FORM_ID = "1FAIpQLSfefqqzKJuPUVvspDvzeFH9bQ4zoRdvQ_B-22kUOahnPFxDgg"; // SamTest
	public static String CLUB_ID = "Robotics";

	private RecyclerView recyclerView;

	private static final int REQUEST_INTERNET = 2;
	public String name = "";

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		ActivityMainBinding binding = ActivityMainBinding.inflate( getLayoutInflater( ) );
		setContentView( binding.getRoot( ) );

		recyclerView = binding.recyclerView;
		setRecyclerView( );
	}

	private void setRecyclerView( ) {

		recyclerView.setAdapter( new PersonAdapter( getPersonNames( ) ) );
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	private void checkInternetPermissions( ) {
		if( checkSelfPermission( Manifest.permission.INTERNET ) == PackageManager.PERMISSION_GRANTED ) {
			submitAttendance( );
		} else {
			if( shouldShowRequestPermissionRationale( Manifest.permission.INTERNET ) )
				Toast.makeText( this, "Accessing internet is required to upload data.", Toast.LENGTH_SHORT ).show( );

			requestPermissions( new String[]{ Manifest.permission.INTERNET }, REQUEST_INTERNET );
		}
	}

	@Override
	public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {
		if( requestCode == REQUEST_INTERNET ) {
			if( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED )
				submitAttendance( );
			else
				Toast.makeText( this, "Permission was not granted. Could not upload data.", Toast.LENGTH_SHORT ).show( );
		}
		super.onRequestPermissionsResult( requestCode, permissions, grantResults );
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	public void submitAttendance( View view ) {
		// check for internet
		name = ((Button) view).getText( ).toString( );
		checkInternetPermissions( );
	}

	public void submitAttendance( ) {

		Person person = new Person( "", "", "", "" );
		for( Person pers : getPersons( ) )
			if( pers.getName( ).equals( name ) )
				person = pers;

		URL url = null;
		try {

			url = FormUtils.getResponseURL(
					MainActivity.FORM_ID,
					Calendar.getInstance( ),
					person.getLastName( ),
					person.getFirstName( ),
					person.getStudentID( ),
					MainActivity.CLUB_ID );

			Log.d( "URL", "" + url );
			Log.d( "PERSON", "" + person );

			Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url.toString( ) ) );
			startActivity( intent );

//		FormUtils.submitURL( url );
		} catch( MalformedURLException e ) {
			e.printStackTrace( );
		}

		FormUtils.submitURL( url );
	}

	public void enterSettings( View view ) {
		FrameLayout parent = ((FrameLayout) view.getParent( ));
		String name = ((Button) parent.getChildAt( 0 )).getText( ).toString( );
		List<Person> persons = getPersons( );
		for( int i = 0; i < persons.size( ); i++ ) {
			if( persons.get( i ).getName( ).equals( name ) ) {
				editPerson( persons.get( i ).getStudentID( ) );
				return;
			}
		}
	}

	public void editPerson( String studentID ) {

		Intent intent = new Intent( this, EditPersonActivity.class );
		intent.putExtra( "STUDENT_ID", studentID );
		startActivity( intent );
	}

	public void addPerson( View view ) {

		createAddPersonList( this );
	}

	private void createAddPersonList( Context context ) {

		AlertDialog.Builder builder = new AlertDialog.Builder( context );
		builder.setTitle( "Enter Contract Id" );
		// Set an EditText view to get user input
		final EditText inputStudentID = new EditText( context );
		inputStudentID.setHint( "Student ID" );
		inputStudentID.setInputType( InputType.TYPE_CLASS_NUMBER );
		final EditText inputFirstName = new EditText( context );
		inputFirstName.setHint( "First Name" );
		final EditText inputLastName = new EditText( context );
		inputLastName.setHint( "Last Name" );

		final LinearLayout linearLayout = new LinearLayout( context );
		linearLayout.setOrientation( LinearLayout.VERTICAL );
		linearLayout.addView( inputStudentID );
		linearLayout.addView( inputFirstName );
		linearLayout.addView( inputLastName );
		//input. // <-- wanted to try and change the underline width of the  input
		builder.setView( linearLayout );

		builder.setPositiveButton( "Create", ( dialog, whichButton ) -> {

			String studentID = inputStudentID.getText( ).toString( );
			String firstName = inputFirstName.getText( ).toString( );
			String lastName = inputLastName.getText( ).toString( );

			DatabaseHelper databaseHelper = new DatabaseHelper( this );
			databaseHelper.addData( new Person( studentID, firstName, lastName, CLUB_ID ) );
			databaseHelper.close( );
			setRecyclerView( );
		} );

		builder.setNegativeButton( "Cancel", ( dialog, whichButton ) -> {
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

	@Override
	public void onResume( ) {

		super.onResume( );
		setRecyclerView( );
	}

}