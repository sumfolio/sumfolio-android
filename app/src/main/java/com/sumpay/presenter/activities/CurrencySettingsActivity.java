package com.sumpay.presenter.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.sumpay.R;
import com.sumpay.presenter.activities.settings.BaseSettingsActivity;
import com.sumpay.presenter.customviews.BaseTextView;
import com.sumpay.presenter.entities.BRSettingsItem;
import com.sumpay.tools.adapter.SettingsAdapter;
import com.sumpay.wallet.WalletsMaster;
import com.sumpay.wallet.abstracts.BaseWalletManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by byfieldj on 2/5/18.
 */

public class CurrencySettingsActivity extends BaseSettingsActivity {

    private BaseTextView mTitle;
    private ListView listView;
    public List<BRSettingsItem> items;

    @Override
    public int getLayoutId() {
        return R.layout.activity_currency_settings;
    }

    @Override
    public int getBackButtonId() {
        return R.id.back_button;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = findViewById(R.id.title);
        listView = findViewById(R.id.settings_list);

        final BaseWalletManager wm = WalletsMaster.getInstance(this).getCurrentWallet(this);

        mTitle.setText(String.format("%s %s", wm.getName(), CurrencySettingsActivity.this.getString(R.string.Settings_title)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (items == null)
            items = new ArrayList<>();
        items.clear();

        items.addAll(WalletsMaster.getInstance(this).getCurrentWallet(this).getSettingsConfiguration().getSettingsList());
        View view = new View(this);
        listView.addFooterView(view, null, true);
        listView.addHeaderView(view, null, true);
        listView.setAdapter(new SettingsAdapter(this, R.layout.settings_list_item, items));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

}
