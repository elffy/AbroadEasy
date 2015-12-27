package com.original.abroadeasy.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.original.abroadeasy.R;


/**
 * @author zengjinlong
 * this is the common layout for view item like below:
 * ______________________________________
 * |                                     |
 * |         CONTENT                     |
 * | <icon>                    <icon>    |
 * |         subContent                  |
 * |_____________________________________|
 * this layout has four custom attributes, see them in R.styleable.TwoRowLayout
 */
public class TwoRowLayout extends RelativeLayout implements View.OnClickListener {

    private static final int LAYOUT_HEIGHT = 50;//50dp
    private static int sHeight;
    private Context mContext;
    private View mItemView;
    private ImageView mRightIcon;
    private TextView mContentView;
    private TextView mSubContentView;
    private String mContent;
    private String mSubContent;

    public TwoRowLayout(Context context) {
        super(context);
    }

    public TwoRowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TwoRowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    /**
     * Initialization process
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void init(Context context, AttributeSet attrs, int defStyle) {
        mContext = context;
        setGravity(Gravity.CENTER_VERTICAL);
        // inflate the content into this layout
        View view = LayoutInflater.from(context).inflate(R.layout.two_row_item_layout, this, true);
        mContentView = (TextView) view.findViewById(R.id.content);
        mSubContentView = (TextView) view.findViewById(R.id.sub_content);
        mRightIcon = (ImageView) view.findViewById(R.id.item_arrow);

        // get the custom attr
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TwoRowLayout, defStyle, 0);
        mContent = a.getString(R.styleable.TwoRowLayout_content);
        mSubContent = a.getString(R.styleable.TwoRowLayout_subContent);
        a.recycle();
        if (mContent != null) {
            mContentView.setText(mContent);
        }
        if (mSubContent != null) {
            mSubContentView.setText(mSubContent);
        }
        this.setOnClickListener(this);
    }


    public void setContent(CharSequence str) {
        mContentView.setText(str);
    }

    public void setOneRowContent(CharSequence str) {
        mContentView.setText(str);
        mSubContentView.setVisibility(View.GONE);
    }

    public void setSubContent(CharSequence str) {
        if (str != null && !str.equals("")) {
            mSubContentView.setText(str);
            mSubContentView.setVisibility(View.VISIBLE);
        }
    }

    public String getSubContent(){
       return mSubContentView.getText().toString().trim();
    }

    private String[] mSubContents;
    public void setSubContent(String[] list) {
        mSubContents = list;
    }
    private int mArrayResId;
    public void setArrayResId(int resId) {
        mArrayResId = resId;
    }

    @Override
    public void onClick(View view) {
        if (mArrayResId != 0) {
            new AlertDialog.Builder(mContext)
                    .setTitle(mContent)
                    .setItems(mArrayResId, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mSubContent = mContext.getResources().getStringArray(mArrayResId)[which];
                            mSubContentView.setText(mSubContent);
                        }
                    })
                    .show();
        }
    }

}
