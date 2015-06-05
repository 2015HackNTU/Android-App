package org.hackntu.hackntu2015.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.treehacks.treehacks.R;

/**
 * Created by weitang114 on 15/6/6.
 */
public class ContactView extends CardView {


    public ContactView(Context context) {
        super(context);
        init(context);
    }

    public ContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ContactView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.card_contact, this);
    }
}
