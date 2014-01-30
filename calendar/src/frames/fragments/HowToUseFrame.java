package frames.fragments;

import com.example.e4d6.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HowToUseFrame extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.how_to_use_frame,container ,false);
	}

}
