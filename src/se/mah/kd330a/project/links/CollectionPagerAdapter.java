package se.mah.kd330a.project.links;

import java.util.List;
import se.mah.kd330a.project.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

@SuppressLint("Recycle")
public class CollectionPagerAdapter extends FragmentStatePagerAdapter {

	TypedArray images;
	Drawable myDrawable;
	Bundle args;
	String[] linkOptions;
	CharSequence pageTitle;

	public CollectionPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new LinksChildFragment();
		args = new Bundle();
		args.putInt("POSITION", i);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return 7;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// fetch the image array
	    
		images = LinksParentFragment.images;
	    myDrawable = images.getDrawable(position);
	    myDrawable.setBounds(0, 1, 128, 128);
	    ImageSpan span = new ImageSpan(myDrawable, ImageSpan.ALIGN_BOTTOM); 
		
		if(position == 0) {
		    linkOptions = LinksParentFragment.linkOptions;
		    SpannableStringBuilder sb = new SpannableStringBuilder(" "+linkOptions[position]+" ");
		    sb.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);; 
		    return sb;
		} else {
		    SpannableStringBuilder sb = new SpannableStringBuilder(" "+" ");
		    sb.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);; 
		    return sb;
		}
	}
}
