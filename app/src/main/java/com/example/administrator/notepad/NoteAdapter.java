package com.example.administrator.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Administrator on 2017/8/25.
 */

public class NoteAdapter extends BaseAdapter{
    private LinkedList<Note> mData;
    private Context mContext;

    public NoteAdapter(LinkedList<Note> mData, Context mContext){
        this.mData = mData;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    } // not important

    @Override
    public long getItemId(int i) {
        return 0;
    } // not important

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        final int maxSummaryLength = 9;
        view = LayoutInflater.from(mContext).inflate(R.layout.note_list_item, parent, false);
        TextView txt_sum = (TextView) view.findViewById(R.id.summary);
        TextView txt_date = (TextView) view.findViewById(R.id.date);

        Note note = mData.get(i);

        /* set txt_sum, summary of the content */
        String content = note.getContent();

        String strOneLine = "";
        int nextLineIndex = content.indexOf("\n");
        if (nextLineIndex == -1)
            strOneLine = content;
        else
            strOneLine = content.substring(0, nextLineIndex);

        String strShortOneLine = strOneLine;
        if (strOneLine.length() <= maxSummaryLength)
            strShortOneLine = strOneLine;
        else{
            strShortOneLine = strOneLine.substring(0, maxSummaryLength) + "...";
        }
        txt_sum.setText(strShortOneLine);
        /*---------------------------------*/

        /* set txt_date, date of the note */
        Date dateCurrent = new Date();
        Date dateNote = new Date(note.getMilliSeconds());

        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");


        String dateCurDayStr = sdfDay.format(dateCurrent);
        String dateNoteDayStr = sdfDay.format(dateNote);

        if (dateCurDayStr.equals(dateNoteDayStr)){
            SimpleDateFormat sdfSecond = new SimpleDateFormat("HH:mm");
            String dateNoteSecondStr = sdfSecond.format(dateNote);
            txt_date.setText(dateNoteSecondStr);

        }
        else
            txt_date.setText(dateNoteDayStr);
        /*---------------------------------*/

        return view;
    }

    private boolean isToday(Calendar cal){
        Calendar calCurrent = Calendar.getInstance();
        return cal.get(Calendar.YEAR) == calCurrent.get(Calendar.YEAR)
                && cal.get(Calendar.MONTH) == calCurrent.get(Calendar.MONTH)
                && cal.get(Calendar.DAY_OF_MONTH) == calCurrent.get(Calendar.DAY_OF_MONTH);
    }

}
