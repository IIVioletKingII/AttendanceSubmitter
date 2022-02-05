package com.example.attendancesubmitter.utilities;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class Person {

	public String firstName;
	public String lastName;
	public String studentID;
	public String club;

	public Person( String studentID, String firstName, String lastName, String club ) {

		this.studentID = studentID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.club = club;
	}

	public void setStudentID( String studentID ) {
		this.studentID = studentID;
	}

	public void setFirstName( String firstName ) {
		this.firstName = firstName;
	}

	public void setLastName( String lastName ) {
		this.lastName = lastName;
	}

	public void setClub( String club ) {
		this.club = club;
	}

	public String getStudentID( ) {
		return studentID;
	}

	public String getFirstName( ) {
		return firstName;
	}

	public String getLastName( ) {
		return lastName;
	}

	public String getName( ) {
		return firstName + " " + lastName;
	}

	public String getClub( ) {
		return club;
	}

	@NonNull
	public String toString( ) {
		return firstName + " " + lastName + " - " + studentID;
	}
}
