package com.kitzapp.telegram_stats.customViews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.common.AppConts;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.simple.TChartDescrTextView;


/**
 * Created by Ivan Kuzmin on 24.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class AuthorView extends LinearLayout {

    public AuthorView(Context context) {
        super(context);
        this.init(context);
    }

    public AuthorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public AuthorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    protected void init(Context context) {
        inflate(context, R.layout.author_cell, this);
        TChartDescrTextView tTextView = findViewById(R.id.authorText);

        tTextView.setTextSizeDP(ThemeManager.TEXT_MEDIUM_SIZE_DP);
        tTextView.setText("Made by Ivan Kuzmin");
        tTextView.setOnClickListener(l -> {
            Intent intentAuthor = new Intent(Intent.ACTION_VIEW);
            intentAuthor.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentAuthor.setData(Uri.parse(AppConts.AUTHOR_LINK));
            context.startActivity(intentAuthor);
        });
    }
}
