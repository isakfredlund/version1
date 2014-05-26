package se.mah.kd330a.project.links;

import se.mah.kd330a.project.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class LinksAdapter extends ArrayAdapter<String> {
    
    Activity context;
    String[] linkTitle;
    int viewPagerPosition;
    String phone = "0709704828";
    String url = "http://www.google.se";

    public LinksAdapter(Activity context, int resource, String[] linkTitle) {
        super(context, resource, linkTitle);
        this.context = context;
        this.linkTitle = linkTitle;
    }
    
    static class ViewHolder {
        TextView textView;
        ImageButton imageButton;
    }
    
    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        View row = convertView;
        
        if(row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.link_list_item, null);
            
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) row.findViewById(R.id.title);
            viewHolder.imageButton = (ImageButton) row.findViewById(R.id.image_button);
            row.setTag(viewHolder);
        }
        
        ViewHolder holder = (ViewHolder) row.getTag();
        String title = linkTitle[pos];
        holder.textView.setText(title);
        holder.textView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                openWebSite(url);
            }
        });
        
        viewPagerPosition = LinksChildFragment.position;
        test(pos, viewPagerPosition, holder);
        
        holder.imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialActivity(phone);
            }
        });
        return row;
    }
    
    private void startDialActivity(String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        context.startActivity(intent);
    }
    
    private void openWebSite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
    
    private void test(int position, int viewPagerPosition, ViewHolder viewHolder) {
        switch (viewPagerPosition) {
            case 1:
                if(position != 3 && position != 5) {
                    viewHolder.imageButton.setVisibility(View.GONE);
                }
            case 3:
                if(position != 0) {
                    viewHolder.imageButton.setVisibility(View.GONE);
                }
        }
    }
}
