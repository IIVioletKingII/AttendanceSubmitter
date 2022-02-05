package com.example.attendancesubmitter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.attendancesubmitter.databinding.ActivityMainBinding;
import com.example.attendancesubmitter.utilities.DatabaseHelper;
import com.example.attendancesubmitter.utilities.FormUtils;
import com.example.attendancesubmitter.utilities.Person;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	public static String FORM_ID = "1FAIpQLScahJirT2sVrm0qDveeuiO1oZBJ5B7J0gdeI7UAZGohKEmi9g";
	public static String CLUB_ID = "Robotics";

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
			url = FormUtils.getResponseURL( FORM_ID, Calendar.getInstance( ), "DePoule", "Sam", "121875", CLUB_ID );
		} catch( MalformedURLException e ) {
			e.printStackTrace( );
		}
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