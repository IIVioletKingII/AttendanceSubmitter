package com.example.attendancesubmitter.utilities;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import io.github.stepio.jgforms.Configuration;
import io.github.stepio.jgforms.Submitter;
import io.github.stepio.jgforms.answer.Builder;
import io.github.stepio.jgforms.exception.NotSubmittedException;
import io.github.stepio.jgforms.question.MetaData;

public class FormUtils {

	public enum AttendanceForm implements MetaData {

		// when clicking on settings or opening the app? set the date to today's date

		DATE( 1621254772L ),
		LAST_NAME( 1341103018L ),
		FIRST_NAME( 951389224L ),
		STUDENT_ID( 591871238L ),
		CLUB( 1390440192L );

		private long id;

		AttendanceForm( long id ) {
			this.id = id;
		}

		@Override
		public long getId( ) {
			return this.id;
		}
	}

	public static URL getResponseURL( String formID, Calendar date, String lastName, String firstName, int studentID, String club ) throws MalformedURLException {

		return Builder.formKey( formID )
				.putDateTime( AttendanceForm.DATE, date )
				.put( AttendanceForm.LAST_NAME, lastName )
				.put( AttendanceForm.FIRST_NAME, firstName )
				.put( AttendanceForm.STUDENT_ID, studentID )
				.put( AttendanceForm.CLUB, club )
				.toUrl( );
	}

	/**
	 *
	 * @param url url to submit
	 * @return true if url was successfully submitted, false otherwise
	 */
	public static boolean submitURL( URL url ) {

		Submitter submitter = new Submitter( new Configuration( ) );
		try {
			submitter.submitForm( url );
		} catch( NotSubmittedException ex ) {
			Log.e( "SUBMIT_URL", "NotSubmittedException: " + ex.getMessage( ) );
			return false;
		}
		return true;
	}

}
