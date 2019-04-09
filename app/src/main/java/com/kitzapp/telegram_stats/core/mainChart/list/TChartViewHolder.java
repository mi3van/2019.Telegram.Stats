package com.kitzapp.telegram_stats.core.mainChart.list;

import android.view.View;
import androidx.annotation.NonNull;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpAdapter.base.BaseMvpViewHolder;
import com.kitzapp.telegram_stats.customViews.TFullCellView;

/**
 * Created by Ivan Kuzmin on 09.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class TChartViewHolder extends BaseMvpViewHolder implements TChartListContract.TViewHolder {

    public TFullCellView tFullCellView;

    public TChartViewHolder(@NonNull View itemView) {
        super(itemView);

        if (tFullCellView == null) {
            tFullCellView = itemView.findViewById(R.id.fullChartCell);
        }
    }

}
