package com.example.attendancesubmitter.utilities;

public class Person {

	public String firstName;
	public String lastName;
	public String studentID;

	public Person( String studentID, String firstName, String lastName ) {

		this.studentID = studentID;
		this.firstName = firstName;
		this.lastName = lastName;
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

	public String getStudentID( ) {
		return studentID;
	}

	public String getFirstName( ) {
		return firstName;
	}

	public String getLastName( ) {
		return lastName;
	}

	public String toString( ) {
		return firstName + ", " + lastName + " -" + studentID;
	}
}
