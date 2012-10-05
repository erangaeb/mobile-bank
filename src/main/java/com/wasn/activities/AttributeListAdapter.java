package com.wasn.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.wasn.pojos.Attribute;

import java.util.ArrayList;

/**
 * List adapter class to display attributes (attributes like key,value pairs)
 *
 * @author erangaeb@gmail.com (eranga bandara)
 */
public class AttributeListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Attribute> attributeList;

    /**
     * Set context and attribute list
     *
     * @param context
     * @param attributeList
     */
    public AttributeListAdapter(Context context, ArrayList<Attribute> attributeList) {
        this.context = context;
        this.attributeList = attributeList;
    }

    /**
     * Get size of attribute list
     */
    public int getCount() {
        return attributeList.size();
    }

    /**
     * Get specific item from attribute list
     */
    public Object getItem(int i) {
        return attributeList.get(i);
    }

    /**
     * Get attribute list item id
     */
    public long getItemId(int i) {
        return i;
    }

    /**
     * Create list row view
     */
    public View getView(int i, View view, ViewGroup viewGroup) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        final ViewHolder holder;

        Attribute attribute = (Attribute) getItem(i);

        if (view == null) {
            //inflate print_list_row layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.attribute_list_row_layout, viewGroup, false);

            //create view holder to store reference to child views
            holder=new ViewHolder();
            holder.attributeNameTextView = (TextView) view.findViewById(R.id.child_detail_list_row_attribute_name);
            holder.attributeValueTextView = (TextView) view.findViewById(R.id.child_detail_list_row_attribute_value);

            view.setTag(holder);
        } else {
            //get view holder back
            holder = (ViewHolder) view.getTag();
        }

        // bind text with view holder text view to efficient use
        holder.attributeNameTextView.setText(attribute.getAttributeName());
        holder.attributeValueTextView.setText(attribute.getAttributeValue());

        return view;
    }

    /**
     * Keep reference to children view to avoid unnecessary calls
     */
    static class ViewHolder {
        TextView attributeNameTextView;
        TextView attributeValueTextView;
    }

}
