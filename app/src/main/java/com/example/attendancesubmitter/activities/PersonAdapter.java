package com.example.attendancesubmitter.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.attendancesubmitter.R;

public class PersonAdapter extends Adapter<PersonAdapter.PersonViewHolder> {

	private final String[] personTilesNamesList;

	@Override
	public PersonViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {

		View view = LayoutInflater.from( parent.getContext( ) ).inflate( R.layout.person_item, parent, false );
		return new PersonViewHolder( view );
	}

	public int getItemCount( ) {
		return this.personTilesNamesList.length;
	}

	public void onBindViewHolder( PersonViewHolder holder, int position ) {
		holder.bind( personTilesNamesList[position] );
	}

	public PersonAdapter( String[] list ) {
		personTilesNamesList = list;
	}

	public static final class PersonViewHolder extends ViewHolder {

		private final TextView personTextView;

		private final int COLOR_GREEN = rgbToInt( 0, 200, 50 );
		private final int COLOR_YELLOW = rgbToInt( 225, 225, 0 );
		private final int COLOR_WHITE = 0xFFFFFFFF;

		public void bind( String word ) {
			personTextView.setText( word );
			personTextView.setTextColor( COLOR_WHITE );
		}

		public PersonViewHolder( View itemView ) {
			super( itemView );
			personTextView = itemView.findViewById( R.id.location_text );
		}
	}

	/**
	 * converts a color from the form 'rgb' to '0xAARRGGBB'
	 *
	 * @param red   the red value of a color
	 * @param green the green value of a color
	 * @param blue  the blue value of a color
	 * @return the color given in form 0xAARRGGBB
	 */
	public static int rgbToInt( int red, int green, int blue ) {
		red = (red << 16) & 0x00FF0000; // Shift red 16-bits and mask out other stuff
		green = (green << 8) & 0x0000FF00; // Shift green 8-bits and mask out other stuff
		blue = blue & 0x000000FF; // Mask out anything not blue.

		return 0xFF000000 | red | green | blue; // 0xFF000000 for 100% Alpha. Bitwise OR everything together.
	}
}