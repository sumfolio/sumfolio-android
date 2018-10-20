package com.sumpay.presenter.activities.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.sumpay.R;
import com.sumpay.presenter.activities.util.BRActivity;
import com.sumpay.presenter.customviews.BRDialogView;
import com.sumpay.tools.animation.UiUtils;
import com.sumpay.tools.animation.BRDialog;
import com.sumpay.tools.manager.BRSharedPrefs;
import com.sumpay.tools.threads.executor.BRExecutor;
import com.sumpay.tools.util.BRConstants;
import com.sumpay.wallet.WalletsMaster;
import com.sumpay.wallet.abstracts.BaseWalletManager;


public class SyncBlockchainActivity extends BRActivity {
    private static final String TAG = SyncBlockchainActivity.class.getName();
    private Button mRescanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_blockchain);

        ImageButton faq = findViewById(R.id.faq_button);

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UiUtils.isClickAllowed()) {
                    return;
                }
                BaseWalletManager wm = WalletsMaster.getInstance(SyncBlockchainActivity.this).getCurrentWallet(SyncBlockchainActivity.this);
                UiUtils.showSupportFragment(SyncBlockchainActivity.this, BRConstants.FAQ_RESCAN, wm);
            }
        });

        mRescanButton = findViewById(R.id.button_scan);
        mRescanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UiUtils.isClickAllowed()) {
                    return;
                }
                BRDialog.showCustomDialog(SyncBlockchainActivity.this, getString(R.string.ReScan_alertTitle),
                        getString(R.string.ReScan_footer), getString(R.string.ReScan_alertAction), getString(R.string.Button_cancel),
                        new BRDialogView.BROnClickListener() {
                            @Override
                            public void onClick(BRDialogView brDialogView) {
                                brDialogView.dismissWithAnimation();
                                rescanClicked();
                            }
                        }, new BRDialogView.BROnClickListener() {
                            @Override
                            public void onClick(BRDialogView brDialogView) {
                                brDialogView.dismissWithAnimation();
                            }
                        }, null, 0);

            }
        });

    }

    private void rescanClicked() {
        BRExecutor.getInstance().forLightWeightBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                Activity thisApp = SyncBlockchainActivity.this;
                BRSharedPrefs.putStartHeight(thisApp, BRSharedPrefs.getCurrentWalletIso(thisApp), 0);
                BRSharedPrefs.putAllowSpend(thisApp, BRSharedPrefs.getCurrentWalletIso(thisApp), false);
                WalletsMaster.getInstance(thisApp).getCurrentWallet(thisApp).rescan(thisApp);
                UiUtils.startBreadActivity(thisApp, false);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

}
