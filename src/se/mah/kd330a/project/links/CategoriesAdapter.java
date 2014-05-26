package se.mah.kd330a.project.links;

import se.mah.kd330a.project.R;
import android.app.Activity;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoriesAdapter extends ArrayAdapter<String> {
    
    Activity context;
    String[] linkTitle;
    String[] linkSubTitle;
    TypedArray images;
    
    public CategoriesAdapter(Activity context, int resource, String[] linkTitle) {
        super(context, resource, linkTitle);
        this.context = context;
        this.linkTitle = linkTitle;
    }
    
    static class ViewHolder {
        TextView textView_1;
        TextView textView_2;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.link_category_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textView_1 = (TextView) row.findViewById(R.id.title);
            viewHolder.textView_2 = (TextView) row.findViewById(R.id.sub_title);
            viewHolder.imageView = (ImageView) row.findViewById(R.id.category_image);
            row.setTag(viewHolder);
        }
        
        ViewHolder holder = (ViewHolder) row.getTag();
        String title = linkTitle[position];
        holder.textView_1.setText(title);
        
        linkSubTitle = LinksParentFragment.subTitles;
        holder.textView_2.setText(linkSubTitle[position]);
        
        images = LinksParentFragment.images;
        holder.imageView.setImageDrawable(images.getDrawable(position+1));
        return row;
    }
}
