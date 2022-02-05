package com.example.attendancesubmitter.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.attendancesubmitter.R;
import com.example.attendancesubmitter.utilities.DatabaseHelper;
import com.example.attendancesubmitter.utilities.DateInputMask;
import com.example.attendancesubmitter.utilities.FormUtils;
import com.example.attendancesubmitter.utilities.Person;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class EditPersonActivity extends AppCompatActivity {

	private String studentID;

	private EditText studentIDText, firstNameText, lastNameText,
			dateText, clubText;

	private static final int REQUEST_INTERNET = 2;
	public String name = "";

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_edit_person );

		studentID = getIntent( ).getStringExtra( "STUDENT_ID" );

		studentIDText = findViewById( R.id.editStudentID );
		firstNameText = findViewById( R.id.editFirstName );
		lastNameText = findViewById( R.id.editLastName );
		dateText = findViewById( R.id.editDate );
		clubText = findViewById( R.id.editClub );

		setStudentID( );
		setName( );
		setDate( );
		setClub( );

		initToolbar( );
	}

	private void initToolbar( ) {

		Toolbar toolbar = findViewById( R.id.edit_toolbar );
		toolbar.setTitle( "" );
		toolbar.setNavigationIcon( ContextCompat.getDrawable( this, R.drawable.back_arrow ) );
		toolbar.setNavigationOnClickListener( view -> finish( ) );
	}


	public void storeStudentID( View view ) {
		storeStudentID( );
	}

	private void storeStudentID( ) {

		DatabaseHelper databaseHelper = new DatabaseHelper( this );

		databaseHelper.store( DatabaseHelper.COLUMN_STUDENT_ID, studentID, studentIDText.length( ) > 0 ? studentIDText.getText( ).toString( ) : "" );

		databaseHelper.close( );
	}

	public void setStudentID( ) {

		Person person = getPerson( );
		studentIDText.setText( person.getStudentID( ) != null ? person.getStudentID( ) : "" );
	}

	public void storeName( View view ) {
		storeName( );
	}

	public void storeName( ) {

		DatabaseHelper databaseHelper = new DatabaseHelper( this );

		databaseHelper.store( DatabaseHelper.COLUMN_FIRST_NAME, studentID, firstNameText.getText( ).toString( ) );
		databaseHelper.store( DatabaseHelper.COLUMN_LAST_NAME, studentID, lastNameText.getText( ).toString( ) );

		databaseHelper.close( );
	}

	public void setName( ) {

		Person person = getPerson( );
		firstNameText.setText( person.getFirstName( ) != null ? person.getFirstName( ) : "" );
		lastNameText.setText( person.getLastName( ) != null ? person.getLastName( ) : "" );
	}

	@SuppressLint("SimpleDateFormat")
	public void setDate( ) {

		new DateInputMask( dateText );
		dateText.setText( new SimpleDateFormat( "MM/dd/yyyy" ).format( Calendar.getInstance( ).getTime( ) ) );
	}

	public void setClub( ) {

		Person person = getPerson( );
		clubText.setText( person.getClub( ) != null ? person.getClub( ) : "" );
	}

	private Calendar getDate( ) {
		int year = Integer.parseInt( dateText.getText( ).toString( ).substring( 6 ) );
		int day = Integer.parseInt( dateText.getText( ).toString( ).substring( 3, 5 ) );
		int month = Integer.parseInt( dateText.getText( ).toString( ).substring( 0, 2 ) );
		Calendar date = Calendar.getInstance( );
		date.set( year, month - 1, day, 0, 0 );
		return date;
	}

	private Person getPerson( ) {
		return getPerson( studentID );
	}

	private Person getPerson( String studentID ) {

		for( Person person : getPersons( ) )
			if( person.getStudentID( ).equals( studentID ) )
				return person;
		return null;
	}

	private List<Person> getPersons( ) {

		DatabaseHelper databaseHelper = new DatabaseHelper( this );
		List<Person> persons = databaseHelper.getPersons( );
		databaseHelper.close( );
		return persons;
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
//			url = FormUtils.getResponseURL( MainActivity.FORM_ID, Calendar.getInstance( ), "DePoule", "Sam", "121875", CLUB_ID );
			url = FormUtils.getResponseURL( MainActivity.FORM_ID, getDate( ), person.getName( ), MainActivity.CLUB_ID );
		} catch( MalformedURLException e ) {
			e.printStackTrace( );
		}

		FormUtils.submitURL( url );
	}

	@Override
	public void onDestroy( ) {
		save( );
		super.onDestroy( );
	}

	@Override
	public void onBackPressed( ) {
		finish( ); // actually closes this activity, returning to the main activity
	}

	public void save( View view ) {
		save( );
	}

	public void save( ) {

		// saves the meter sizes
		storeStudentID( );
		// saves the meter summaries
		storeName( );
	}

	public void delete( View view ) {
		createAddPersonList( this );
	}


	private void createAddPersonList( Context context ) {

		AlertDialog.Builder builder = new AlertDialog.Builder( context );
		builder.setTitle( "Are you sure you want to delete this person?" );
		builder.setPositiveButton( "Delete", ( dialog, whichButton ) -> delete( ) );
		builder.setNegativeButton( "Cancel", ( dialog, whichButton ) -> { /*Canceled, exit*/ } );
		builder.show( );
	}

	public void delete( ) {
		DatabaseHelper databaseHelper = new DatabaseHelper( this );
		databaseHelper.deletePerson( studentID );
		databaseHelper.close( );
		// go back to main menu
		finish( );
	}


}