package com.eventsproject.byui_events;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import org.w3c.dom.Text;
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
    private Context context;
    private List<String> titleList;
    private Map<String, String> childList;

    /*
     * MEMBER METHODS
     */


    /**
     * CONSTRUCTOR
     * @param context
     * @param list
     * @param childList
     */
    public ExpandableListViewAdapter(Context context, List<String> list,
                                     Map<String, String> childList) {
        this.context = context;
        this.titleList = list;
        this.childList = childList;
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
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //now convert it!
            view = layoutInflater.inflate(R.layout.list_child_view, null);
        }


        //now add the child text!
        TextView childView = (TextView) view.findViewById(R.id.list_child_view);

        //now set the text!
        childView.setText(childText);

        return childView;
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
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //now convert it!
            view = layoutInflater.inflate(R.layout.list_view, null);
        }

        //now add the title text!
        TextView titleView = (TextView) view.findViewById(R.id.list_view);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setText(title);

        return titleView;
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
