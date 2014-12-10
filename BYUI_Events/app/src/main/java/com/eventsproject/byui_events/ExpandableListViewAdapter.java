package com.eventsproject.byui_events;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by SamuelHibbard on 12/1/14.
 */
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    /*
     * MEMBER VARIABLES
     */
    private List<String> titleList;
    private Map<String, String> childList;
    private List<byte[]> images;
    private LayoutInflater inflater;

    /*
     * MEMBER METHODS
     */


    /**
     * CONSTRUCTOR
     * @param activity
     * @param list
     * @param childList
     */
    public ExpandableListViewAdapter(Activity activity, List<String> list,
                                     Map<String, String> childList, List<byte[]> images) {
        this.titleList = list;
        this.childList = childList;
        this.images = images;
        inflater = activity.getLayoutInflater();
        //Log.d("SQL_images_size: ", Integer.toString(images.size()));
    }

    /**
     * GETCHILDVIEW
     *  Grab the child of the certain list_view.
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param view
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {
        //grab the child text!
        String childText = (String) getChild(groupPosition, childPosition);

        //now put it into the view!
        if (view == null) {
            //(LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //now convert it!
            view = inflater.inflate(R.layout.list_child_view, null);
        }


        //now add the child text!
        TextView childView = (TextView) view.findViewById(R.id.list_child_view);

        //now set the text!
        childView.setText(childText);

        return view;
    }

    /**
     * GETGROUPVIEW
     *  This will put the view into a header.
     * @param groupPosition
     * @param isExpanded
     * @param view
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view,
                             ViewGroup parent) {
        //grab the title!
        String title = (String) getGroup(groupPosition);

        //check the VIEW to see if it is null
        if (view == null) {
//            LayoutInflater layoutInflater = LayoutInflater.from(context);
//            LayoutInflater layoutInflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //now convert it!
            view = inflater.inflate(R.layout.list_view, null);
        }

        //check to make sure there is something there!
        byte[] image = getImage(groupPosition);
        if (image != null && image.length > 0) {
            Log.d("Image: ", Integer.toString(image.length));
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view);

            //convert the bytes to an image!
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

            if (imageView != null) {
                //now set the image!
                Log.d("Bitmap:", bitmap.toString());
                imageView.setImageBitmap(bitmap);
            } else {
                Log.d("IMAGEVIEW", "NULL");
            }
        }

        //now add the title text!
        TextView titleView = (TextView) view.findViewById(R.id.list_view);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setText(title);

        return view;
    }

    /**
     * GETCHILD
     *  Grab the child!
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(titleList.get(groupPosition));
    }

    /**
     * GETIMAGE
     *  Grab the image!
     * @param groupPosition
     * @return
     */
    public byte[] getImage(int groupPosition) {
        return images.get(groupPosition);
    }

    /**
     * ISCHILDSELECTABLE
     *   Yes for all of them!
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * GETGROUPCOUNT
     * @return
     */
    @Override
    public int getGroupCount() {
        return titleList.size();
    }

    /**
     * HASSTABLEIDS
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * GETGROUPID
     * @param groupPosition
     * @return
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * GETGROUP
     * @param groupPosition
     * @return
     */
    @Override
    public Object getGroup(int groupPosition) {
        return titleList.get(groupPosition);
    }

    /**
     * GETCHILDRENCOUNT
     * @param childPosition
     * @return
     */
    @Override
    public int getChildrenCount(int childPosition) {
        return 1;
    }

    /**
     * GETCHILDID
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

}
