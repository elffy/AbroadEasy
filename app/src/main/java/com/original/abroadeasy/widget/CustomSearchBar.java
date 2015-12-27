
package com.original.abroadeasy.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.original.abroadeasy.R;

public class CustomSearchBar extends RelativeLayout implements OnClickListener{

    private Context mContext;
    private RelativeLayout mSearchBarView;

    private TextView mBtnClearSearch;

    private LinearLayout mSearchView;
    private EditText mSearchEdit;
    private boolean mIsSearchMode = false;
    private boolean mIsPlaySearchModeAnimation = false;

    private SearchBarListener mListener;
    public interface SearchBarListener {

        void onQueryTextChange(String newString);

        void cancelSearch();

        /**
         * the search button on input panel clicked.
         * @param queryText
         */
        void startSearch(String queryText);
    }

    public CustomSearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSearchBarView = (RelativeLayout) inflater.inflate(R.layout.custom_searchbar,
                this, true);
        setViews();
    }

    private void setViews() {
        mSearchView = (LinearLayout) mSearchBarView.findViewById(R.id.search_view);
        mSearchView.setOnClickListener(this);
        mBtnCancelSearch = (TextView) findViewById(R.id.btn_cancel);
        mBtnCancelSearch.setOnClickListener(this);

        mSearchEdit = (EditText) mSearchBarView.findViewById(R.id.search_edit_text);
        mSearchEdit.addTextChangedListener(mSearchWatcher);
        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && mListener != null) {
                    mListener.startSearch(mSearchEdit.getText().toString().trim());
                }
                return false;
            }
        });
        mSearchEdit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mBtnClearSearch = (TextView) mSearchBarView.findViewById(R.id.btn_clear_search);
        mBtnClearSearch.setOnClickListener(this);
    }

    /* package */void clearSearchText() {
        if (mSearchEdit != null) {
            mSearchEdit.setText("");
        }
    }

    private TextWatcher mSearchWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mIsSearchMode && mListener != null) {
                mListener.onQueryTextChange(String.valueOf(s));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            mBtnClearSearch.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
        }
    };

    public void setListener(SearchBarListener listener) {
        mListener = listener;
    }

    public void clearState(){
        TextKeyListener.clear(mSearchEdit.getText());
    }
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_cancel:
                cancelSearch();
                break;
            case R.id.btn_clear_search:
                mSearchEdit.setText("");
                break;
        }
    }

    public void requestSeachViewFocus() {
        mSearchEdit.requestFocus();
    }
    private TextView mBtnCancelSearch;
    public void startSearch() {
        if (!mIsPlaySearchModeAnimation && !mIsSearchMode) {
            mSearchEdit.setCursorVisible(true);
            mSearchEdit.setFocusable(true);
            mSearchEdit.setFocusableInTouchMode(true);
            mSearchEdit.requestFocus();
            postDelayed(new Runnable() {
                public void run() {
                    InputMethodManager imm = (InputMethodManager) mContext
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mSearchEdit, 0);
                }
            }, 200);
            mIsSearchMode = true;
        }
    }

    private void hideInputPannel() {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchEdit.getWindowToken(), 0);
    }

    public void cancelSearch() {
        hideInputPannel();
        post(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.cancelSearch();
                }
            }
        });
        TextKeyListener.clear(mSearchEdit.getText());
        mIsSearchMode = false;
    }
}
