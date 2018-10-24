package com.sumfolio.wallet.wallets.sumcoin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.sumfolio.BuildConfig;
import com.sumfolio.core.BRCoreAddress;
import com.sumfolio.core.BRCoreChainParams;
import com.sumfolio.core.BRCoreMasterPubKey;
import com.sumfolio.tools.manager.BREventManager;
import com.sumfolio.tools.manager.BRSharedPrefs;
import com.sumfolio.tools.security.BRKeyStore;
import com.sumfolio.tools.threads.executor.BRExecutor;
import com.sumfolio.tools.util.BRConstants;
import com.sumfolio.tools.util.SettingsUtil;
import com.sumfolio.tools.util.Utils;
import com.sumfolio.wallet.WalletsMaster;
import com.sumfolio.wallet.configs.WalletSettingsConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * BreadWallet
 * <p/>
 * Created by Mihail Gutan on <mihail@sumfolio.com> 1/22/18.
 * Copyright (c) 2018 sumfolio LLC
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
public final class WalletBchManager extends BaseSumcoinWalletManager {

    private static final String TAG = WalletBchManager.class.getName();

    private static final String ISO = BITCASH_SYMBOL;
    private static final String NAME = "Sumcoin Cash";
    private static final String SCHEME = BuildConfig.SUMCOIN_TESTNET ? "bchtest" : "bitcoincash";
    private static final String COLOR = "#478559";
    private static final long MAINNET_FORK_TIME = 1501568580; // Tuesday, August 1, 2017 6:23:00 AM GMT in seconds since Epoch
    private static final long TESTNET_FORK_TIME = 1501597117; // Tuesday, August 1, 2017 2:18:37 PM GMT in seconds since Epoch

    private static WalletBchManager mInstance;

    public static synchronized WalletBchManager getInstance(Context context) {
        if (mInstance == null) {
            byte[] rawPubKey = BRKeyStore.getMasterPublicKey(context);
            if (Utils.isNullOrEmpty(rawPubKey)) {
                Log.e(TAG, "getInstance: rawPubKey is null");
                return null;
            }
            BRCoreMasterPubKey pubKey = new BRCoreMasterPubKey(rawPubKey, false);
            long time = BRKeyStore.getWalletCreationTime(context);
            if (!BRSharedPrefs.getBchPreforkSynced(context) && time == 0)
                time = BuildConfig.SUMCOIN_TESTNET ? TESTNET_FORK_TIME : MAINNET_FORK_TIME;
            mInstance = new WalletBchManager(context, pubKey, BuildConfig.SUMCOIN_TESTNET ?
                    BRCoreChainParams.testnetBcashChainParams : BRCoreChainParams.mainnetBcashChainParams, time);
        }
        return mInstance;
    }

    private WalletBchManager(final Context context, BRCoreMasterPubKey masterPubKey,
                             BRCoreChainParams chainParams,
                             double earliestPeerTime) {
        super(context, masterPubKey, chainParams, earliestPeerTime);
        BRExecutor.getInstance().forLightWeightBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                if (BRSharedPrefs.getStartHeight(context, getIso()) == 0)
                    BRSharedPrefs.putStartHeight(context, getIso(), getPeerManager().getLastBlockHeight());

                BigDecimal fee = BRSharedPrefs.getFeeRate(context, getIso());
                BigDecimal economyFee = BRSharedPrefs.getEconomyFeeRate(context, getIso());
                if (fee.compareTo(BigDecimal.ZERO) == 0) {
                    fee = new BigDecimal(getWallet().getDefaultFeePerKb());
                    BREventManager.getInstance().pushEvent("wallet.didUseDefaultFeePerKB");
                }
                getWallet().setFeePerKb(BRSharedPrefs.getFavorStandardFee(context, getIso()) ? fee.longValue() : economyFee.longValue());
            }
        });
        WalletsMaster.getInstance(context).setSpendingLimitIfNotSet(context, this);

        setSettingsConfig(new WalletSettingsConfiguration(context, getIso(), SettingsUtil.getSumcoinCashSettings(context), getFingerprintLimits(context)));
//          BRPeerManager.getInstance().updateFixedPeer(ctx);//todo reimplement the fixed peer
    }

    protected String getTag() {
        return TAG;
    }

    @Override
    protected String getColor() {
        return COLOR;
    }

    protected List<BigDecimal> getFingerprintLimits(Context app) {
        List<BigDecimal> result = new ArrayList<>();
        result.add(new BigDecimal(ONE_SUMCOIN_IN_SATOSHIS).divide(new BigDecimal(100), getMaxDecimalPlaces(app), BRConstants.ROUNDING_MODE));
        result.add(new BigDecimal(ONE_SUMCOIN_IN_SATOSHIS).divide(new BigDecimal(10), getMaxDecimalPlaces(app), BRConstants.ROUNDING_MODE));
        result.add(new BigDecimal(ONE_SUMCOIN_IN_SATOSHIS));
        result.add(new BigDecimal(ONE_SUMCOIN_IN_SATOSHIS).multiply(new BigDecimal(10)));
        result.add(new BigDecimal(ONE_SUMCOIN_IN_SATOSHIS).multiply(new BigDecimal(100)));
        return result;
    }

    @Override
    public String getIso() {
        return ISO;
    }

    @Override
    public String getScheme() {
        return SCHEME;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String decorateAddress(String addr) {
        if (Utils.isNullOrEmpty(addr)) return null;
        String result = BRCoreAddress.bcashEncodeSumcoin(addr);
        if (Utils.isNullOrEmpty(result)) return null;
        if (result.contains(":")) return result.split(":")[1]; //bitcoincash:q24fs34....
        return result;

    }

    @Override
    public String undecorateAddress(String addr) {
        if (Utils.isNullOrEmpty(addr)) return null;
        String result = BRCoreAddress.bcashDecodeSumcoin(addr);
        return Utils.isNullOrEmpty(result) ? null : result;

    }

    protected void syncStopped(Context context) {
        BRSharedPrefs.putBchPreforkSynced(context, true);
    }
}
