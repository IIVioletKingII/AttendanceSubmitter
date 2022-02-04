package com.example.attendancesubmitter.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String PERSONS_TABLE = "LOCATION_TABLE";
	public static final String COLUMN_FIRST_NAME = "FIRST_NAME";
	public static final String COLUMN_LAST_NAME = "LAST_NAME";
	public static final String COLUMN_STUDENT_ID = "STUDENT_ID";

	public DatabaseHelper( Context context ) {
		super( context, "persons.db", null, 1 );
	}

	// this is called the first time a database is accessed. There should be code in here to create a new database.
	@Override
	public void onCreate( SQLiteDatabase db ) {
		String createTableStatement = "CREATE TABLE " + PERSONS_TABLE + " ("
				+ COLUMN_STUDENT_ID + " TEXT PRIMARY KEY,"
				+ COLUMN_FIRST_NAME + " TEXT,"
				+ COLUMN_LAST_NAME + " TEXT"
				+ ");";

		db.execSQL( createTableStatement );

	}

	// this is called if the database version number changes. It prevents users apps from breaking when you create the database design.
	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		db.execSQL( "DROP TABLE " + PERSONS_TABLE );
		onCreate( db );
	}

	public void addData( Person person ) {

		SQLiteDatabase database = this.getWritableDatabase( );
		ContentValues contentValues = new ContentValues( );

		contentValues.put( COLUMN_STUDENT_ID, person.getStudentID( ) );
		contentValues.put( COLUMN_FIRST_NAME, person.getFirstName( ) );
		contentValues.put( COLUMN_LAST_NAME, person.getLastName( ) );

		database.insert( PERSONS_TABLE, null, contentValues );

		database.close( );
	}

    
    /*
    methods for:
    
	COLUMN_FIRST_NAME
	COLUMN_LAST_NAME
	COLUMN_STUDENT_ID
     */

	/**
	 * @param column    the column to store data into
	 * @param studentID the id of the student to edit
	 * @param newString the new data to put in <i>column</i>
	 */
	public void store( String column, String studentID, String newString ) {

		SQLiteDatabase database = this.getWritableDatabase( );
		ContentValues cv = new ContentValues( );

		cv.put( column, newString );
		database.update( PERSONS_TABLE, cv, COLUMN_STUDENT_ID + " = '" + studentID + "'", null );
	}

	/**
	 * @param column    the column to store data into
	 * @param studentID the id of the student to edit
	 * @param newDouble the new data to put in <i>column</i>
	 */
	public void store( String column, String studentID, Double newDouble ) {

		SQLiteDatabase database = this.getWritableDatabase( );
		ContentValues cv = new ContentValues( );

		cv.put( column, newDouble );
		database.update( PERSONS_TABLE, cv, COLUMN_STUDENT_ID + " = '" + studentID + "'", null );
	}

	/**
	 * @param column    the column to store data into
	 * @param studentID the id of the student to edit
	 * @param newLong   the new data to put in <i>column</i>
	 */
	public void store( String column, String studentID, long newLong ) {

		SQLiteDatabase database = this.getWritableDatabase( );
		ContentValues cv = new ContentValues( );

		cv.put( column, newLong );
		database.update( PERSONS_TABLE, cv, COLUMN_STUDENT_ID + " = '" + studentID + "'", null );
	}

	/**
	 * @param column     the column to store data into
	 * @param studentID  the id of the student to edit
	 * @param newBoolean the new data to put in <i>column</i>
	 */
	public void store( String column, String studentID, boolean newBoolean ) {

		SQLiteDatabase database = this.getWritableDatabase( );
		ContentValues cv = new ContentValues( );

		cv.put( column, (newBoolean ? 1 : 0) );
		database.update( PERSONS_TABLE, cv, COLUMN_STUDENT_ID + " = '" + studentID + "'", null );
	}

	/**
	 * @return a list of names in the data base like this
	 * "firstName, lastName"
	 */
	public List<String> getNames( ) {

		List<String> returnList = new ArrayList<>( );

		// Get data from database
		String queryString = "SELECT * FROM " + PERSONS_TABLE;

		SQLiteDatabase database = this.getReadableDatabase( );

		Cursor cursor = database.rawQuery( queryString, null );

		if( cursor.moveToFirst( ) ) {
			// Loop through the cursor (result set) and get addresses. Put them into the return list.
			do {
				String name = "";
				name += returnList.add( cursor.getString( 1 ) );
				name += ", ";
				name += returnList.add( cursor.getString( 2 ) );

			} while( cursor.moveToNext( ) );
		} else {
			// Failure. Do not add anything to the list.
			Log.e( "DATABASE_GET_NAMES", "Couldn't loop through the database to get names" );
		}

		// Close both the cursor and the database when done.
		cursor.close( );
		database.close( );

		return returnList;
	}

	/**
	 * @return a list of persons in the database
	 */
	public List<Person> getPersons( ) {

		List<Person> returnList = new ArrayList<>( );

		// Get data from database
		String queryString = "SELECT * FROM " + PERSONS_TABLE;

		SQLiteDatabase database = this.getReadableDatabase( );

		Cursor cursor = database.rawQuery( queryString, null );

		if( cursor.moveToFirst( ) ) {
			// Loop through the cursor (result set) and create new location objects. Put them into the return list.
			do {

				String studentID = cursor.getString( 0 );
				String firstName = cursor.getString( 1 );
				String lastName = cursor.getString( 2 );

				returnList.add( new Person( studentID, firstName, lastName ) );

			} while( cursor.moveToNext( ) );
		} else {
			// Failure. Do not add anything to the list.
			Log.e( "DATABASE_GET_PERSONS", "Couldn't loop through the database to get persons" );
		}

		// Close both the cursor and the database when done.
		cursor.close( );
		database.close( );

		return returnList;
	}

	/**
	 * deletes all data for the person with the studentID
	 * @param studentID passed in
	 */
	public void deletePerson( String studentID ) {

		SQLiteDatabase database = this.getWritableDatabase( );
		String queryString = "DELETE FROM " + PERSONS_TABLE + " WHERE " + COLUMN_STUDENT_ID + " = '" + studentID + "'";

		Cursor cursor = database.rawQuery( queryString, null );

		cursor.moveToFirst( );

		cursor.close( );
		database.close( );

	}

}

