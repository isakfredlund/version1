package se.mah.kd330a.project.links;

import android.annotation.SuppressLint;
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
	    linkOptions = LinksParentFragment.linkOptions;
	    SpannableStringBuilder sb = null ;
		
	    if(position==0){
	    return linkOptions[0];
	    } else {
		    sb = new SpannableStringBuilder(" "+" ");
		    myDrawable = images.getDrawable(position);
		    myDrawable.setBounds(0, 1, 96, 96); 
		
		    ImageSpan span = new ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE); 
		    sb.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    }
	    return sb;
	}
}
