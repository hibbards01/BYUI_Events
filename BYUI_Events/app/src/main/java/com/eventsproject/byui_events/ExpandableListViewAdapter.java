package com.eventsproject.byui_events;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
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
    private Map<String, String[]> dateList;
    private LayoutInflater inflater;
    private String activity;

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
                                     Map<String, String> childList,
                                     List<byte[]> images,
                                     Map<String, String[]> dateList,
                                     String type) {
        this.titleList = list;
        this.childList = childList;
        this.images = images;
        this.dateList = dateList;
        inflater = activity.getLayoutInflater();
        this.activity = type;
    }

    /**
     * SETLISTS
     *  Set the lists!
     * @param list
     * @param childList
     * @param images
     * @param dateList
     */
    public void setLists(List<String> list, Map<String, String> childList, List<byte[]> images,
                         Map<String, String[]> dateList) {
        this.titleList = list;
        this.childList = childList;
        this.images = images;
        this.dateList = dateList;
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
        String textDate;

        //inflate the view if null!
        if (view == null) {
            //now convert it!
            view = inflater.inflate(R.layout.list_view, null);
        }
        TextView titleView = (TextView) view.findViewById(R.id.list_view);


        //and parse string correctly!
        String[] split = title.split("~");

        if (split.length > 1) {
            title = split[1];
            //now grab the date!
            String[] date = getDate(groupPosition);

            //grab the date!
            if (activity.equals("DAY")) {
                textDate = date[1];

            } else {
                //split the date into just month and day!
                String[] splitDate = date[0].split("-");
                textDate = splitDate[1] + "/" + splitDate[2] + " " + date[1];
            }

            //check to make sure there is something there!
            byte[] image = getImage(groupPosition);

            ImageView imageView = (ImageView) view.findViewById(R.id.image_view);

            //convert the bytes to an image!
            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

                //now set the image!
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageDrawable(null);
            }

            //now add the title text!
            titleView.setText(Html.fromHtml("<b>" + title + "</b><br />" + textDate));
        } else {
            titleView.setText("No events saved");
        }

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
     * GETDATE
     *  Grab the date!
     * @param groupPosition
     * @return
     */
    public String[] getDate(int groupPosition) {
        return dateList.get(titleList.get(groupPosition));
    }

    /**
     * GETIMAGE
     *  Grab the image!
     * @param groupPosition
     * @return
     */
    public byte[] getImage(int groupPosition) {
        if (images.size() > 0) {
            return images.get(groupPosition);
        }

        return null;
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
