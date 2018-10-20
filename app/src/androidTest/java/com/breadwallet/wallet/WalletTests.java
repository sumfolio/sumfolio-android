package com.sumpay.wallet;

import android.app.Activity;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.sumpay.BreadApp;
import com.sumpay.core.BRCoreMasterPubKey;
import com.sumpay.presenter.activities.settings.TestActivity;
import com.sumpay.presenter.entities.CurrencyEntity;
import com.sumpay.presenter.entities.CryptoRequest;
import com.sumpay.tools.manager.BRSharedPrefs;
import com.sumpay.tools.security.BRKeyStore;
import com.sumpay.tools.sqlite.RatesDataSource;
import com.sumpay.tools.threads.executor.BRExecutor;
import com.sumpay.wallet.abstracts.BaseWalletManager;
import com.sumpay.wallet.wallets.sumcoin.WalletBchManager;
import com.sumpay.wallet.util.CryptoUriParser;
import com.sumpay.tools.util.BRConstants;
import com.sumpay.wallet.wallets.sumcoin.WalletSumcoinManager;
import com.sumpay.wallet.wallets.ethereum.WalletEthManager;
import com.sumpay.wallet.wallets.ethereum.WalletTokenManager;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;


/**
 * BreadWallet
 * <p/>
 * Created by Mihail Gutan on 4/29/16.
 * Copyright (c) 2016 sumpay llc <mihail@sumpay.com>
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

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WalletTests {
    public static final String TAG = WalletTests.class.getName();

    @Rule
    public ActivityTestRule<TestActivity> mActivityRule = new ActivityTestRule<>(TestActivity.class);


    // Test Wallets
    BaseWalletManager mBtcWallet;
    BaseWalletManager mBchWallet;

    @Before
    public void setUp() {
        Log.e(TAG, "setUp: ");
        BRCoreMasterPubKey pubKey = new BRCoreMasterPubKey("cat circle quick rotate arena primary walnut mask record smile violin state".getBytes(), true);
        BRKeyStore.putMasterPublicKey(pubKey.serialize(), mActivityRule.getActivity());
        mBtcWallet = WalletSumcoinManager.getInstance(mActivityRule.getActivity());
        mBchWallet = WalletBchManager.getInstance(mActivityRule.getActivity());
    }

    @After
    public void tearDown() {
    }

    static {
        System.loadLibrary(BRConstants.NATIVE_LIB_NAME);
    }

    public void exchangeTests() {
        long satoshis = 50000000;
        //todo finish tests
    }

    @Test
    public void paymentRequestTest() throws InvalidAlgorithmParameterException {
        Activity app = mActivityRule.getActivity();
        CryptoRequest obj = CryptoUriParser.parseRequest(app, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        assertEquals("n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi", obj.address);

//        r = [BRPaymentRequest requestWithString:@"1SUMorgHwCg6u2YSAWKgS17qUad6kHmtQ"];
//        XCTAssertFalse(r.isValid);
//        XCTAssertEqualObjects(@"sumcoin:1SUMorgHwCg6u2YSAWKgS17qUad6kHmtQ", r.string,
//        @"[BRPaymentRequest requestWithString:]");

        obj = CryptoUriParser.parseRequest(app, "sumcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        assertEquals(obj.address, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");

        obj = CryptoUriParser.parseRequest(app, "sumcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=1");
        assertEquals(obj.address, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        BigDecimal bigDecimal = obj.amount;
        long amountAsLong = bigDecimal.longValue();
        assertEquals(String.valueOf(amountAsLong), "100000000");

        obj = CryptoUriParser.parseRequest(app, "sumcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=0.00000001");
        assertEquals(obj.address, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        bigDecimal = obj.amount;
        amountAsLong = bigDecimal.longValue();
        assertEquals(String.valueOf(amountAsLong), "1");

        obj = CryptoUriParser.parseRequest(app, "sumcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=100000000");
        assertEquals(obj.address, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        bigDecimal = obj.amount;
        amountAsLong = bigDecimal.longValue();
        assertEquals(String.valueOf(amountAsLong), "10000000000000000");

        // test for floating point rounding issues, these values cannot be exactly represented with an IEEE 754 double
        obj = CryptoUriParser.parseRequest(app, "sumcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=20999999.99999999");
        assertEquals(obj.address, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        bigDecimal = obj.amount;
        amountAsLong = bigDecimal.longValue();
        assertEquals(String.valueOf(amountAsLong), "2099999999999999");

        obj = CryptoUriParser.parseRequest(app, "sumcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=20999999.99999995");
        assertEquals(obj.address, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        bigDecimal = obj.amount;
        amountAsLong = bigDecimal.longValue();
        assertEquals(String.valueOf(amountAsLong), "2099999999999995");

        obj = CryptoUriParser.parseRequest(app, "sumcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=0.07433");
        assertEquals(obj.address, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        bigDecimal = obj.amount;
        amountAsLong = bigDecimal.longValue();
        assertEquals(String.valueOf(amountAsLong), "7433000");

        // invalid amount string
        obj = CryptoUriParser.parseRequest(app, "sumcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=foobar");
        assertEquals(obj.address, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        assertEquals(obj.amount, null);

        // test correct encoding of '&' in argument value
        obj = CryptoUriParser.parseRequest(app, "sumcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?label=foo%26bar");
        assertEquals(obj.address, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        assertEquals(obj.label, "foo");

        // test handling of ' ' in label or message
        obj = CryptoUriParser.parseRequest(app, "sumcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?label=foo bar&message=bar foo");
        assertEquals(obj.address, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        assertEquals(obj.label, "foo bar");
        assertEquals(obj.message, "bar foo");

        // test bip73
        obj = CryptoUriParser.parseRequest(app, "sumcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?r=https://foobar.com");
        assertEquals(obj.address, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        assertEquals(obj.r, "https://foobar.com");

        obj = CryptoUriParser.parseRequest(app, "sumcoin:?r=https://foobar.com");
        assertEquals(obj.address, null);
        assertEquals(obj.r, "https://foobar.com");
    }

    @Test
    public void currencyManagerTests() {

    }

    @Test
    public void walletSumcoinTests() {
        Activity app = mActivityRule.getActivity();

        WalletSumcoinManager sumWallet = WalletSumcoinManager.getInstance(app);

        BRSharedPrefs.putPreferredFiatIso(app, "USD");

        int sumRate = 12000;
        float ethRate = 0.05f;
        float brdRate = 0.00005f;

        Set<CurrencyEntity> tmp = new HashSet<>();
        tmp.add(new CurrencyEntity("USD", "Dollar", sumRate, "SUM"));
        tmp.add(new CurrencyEntity("SUM", "Sumcoin", ethRate, "ETH"));
        tmp.add(new CurrencyEntity("SUM", "Sumcoin", brdRate, "BRD"));
        RatesDataSource.getInstance(app).putCurrencies(app, tmp);

        BRSharedPrefs.putCryptoDenomination(app, "SUM", BRConstants.CURRENT_UNIT_BITCOINS);

        //getCryptoForSmallestCrypto(..)
        BigDecimal val = new BigDecimal(20000);
        BigDecimal res = sumWallet.getCryptoForSmallestCrypto(app, val);
        Assert.assertEquals(res.doubleValue(), new BigDecimal(0.0002).doubleValue(), 0.000000001);

        //getSmallestCryptoForCrypto(..)
        val = new BigDecimal(0.5);
        res = sumWallet.getSmallestCryptoForCrypto(app, val);
        Assert.assertEquals(res.longValue(), 50000000, 0);

        //getFiatForSmallestCrypto(..)
        val = sumWallet.getSmallestCryptoForCrypto(app, new BigDecimal(0.5));
        res = sumWallet.getFiatForSmallestCrypto(app, val, null);
        Assert.assertEquals(res.doubleValue(), sumRate / 2, 0); //dollars

        //getSmallestCryptoForFiat(..)
        val = new BigDecimal(6000);//$6000.00 = c600000
        res = sumWallet.getSmallestCryptoForFiat(app, val);
        Assert.assertTrue(res.compareTo(new BigDecimal(50000000)) == 0); //dollars

        //getCryptoForFiat(..)
        val = new BigDecimal(6000);//$6000.00 = c600000
        res = sumWallet.getCryptoForFiat(app, val);
        Assert.assertEquals(res.doubleValue(), 0.5, 0); //dollars


        BRSharedPrefs.putCryptoDenomination(app, "SUM", BRConstants.CURRENT_UNIT_BITS);

        //getCryptoForSmallestCrypto(..)
        val = new BigDecimal(20000);
        res = sumWallet.getCryptoForSmallestCrypto(app, val);
        Assert.assertEquals(res.doubleValue(), new BigDecimal(200).doubleValue(), 0.000000001);

        //getSmallestCryptoForCrypto(..)
        val = new BigDecimal(200);
        res = sumWallet.getSmallestCryptoForCrypto(app, val);
        Assert.assertEquals(res.longValue(), 20000, 0);

        //getFiatForSmallestCrypto(..)
        val = new BigDecimal(50000000);
        res = sumWallet.getFiatForSmallestCrypto(app, val, null);
        Assert.assertEquals(res.doubleValue(), sumRate / 2, 0); // dollars

        //getSmallestCryptoForFiat(..)
        val = new BigDecimal(6000);//$6000.00 = c600000
        res = sumWallet.getSmallestCryptoForFiat(app, val);
        Assert.assertEquals(res.doubleValue(), 50000000, 0); //dollars

        //getCryptoForFiat(..)
        val = new BigDecimal(6000);//$6000.00 = c600000
        res = sumWallet.getCryptoForFiat(app, val);
        Assert.assertTrue(res.compareTo(new BigDecimal(500000)) == 0); //dollars

        //TEST ETH
        WalletEthManager ethWallet = WalletEthManager.getInstance(app);
        Assert.assertNotNull(ethWallet);

        //getCryptoForSmallestCrypto(..)
        val = new BigDecimal("25000000000000000000");
        res = ethWallet.getCryptoForSmallestCrypto(app, val);
        Assert.assertTrue(res.compareTo(new BigDecimal(25)) == 0);

        //getSmallestCryptoForCrypto(..)
        val = new BigDecimal(25);
        res = ethWallet.getSmallestCryptoForCrypto(app, val);
        Assert.assertTrue(res.toPlainString().compareTo(new BigDecimal("25000000000000000000").toPlainString()) == 0);

        //getFiatForSmallestCrypto(..)
        val = new BigDecimal("25000000000000000000");
        res = ethWallet.getFiatForSmallestCrypto(app, val, null);
        Assert.assertEquals(res.doubleValue(), sumRate * ethRate * 25, 0.001); //dollars

        //getSmallestCryptoForFiat(..)
        val = new BigDecimal(ethRate).multiply(new BigDecimal(3)).multiply(new BigDecimal(sumRate));//
        res = ethWallet.getSmallestCryptoForFiat(app, val);
        Assert.assertTrue(res.compareTo(new BigDecimal("3000000000000000000")) == 0);

        //getCryptoForFiat(..)
        val = new BigDecimal(ethRate).multiply(new BigDecimal(3)).multiply(new BigDecimal(sumRate));
        res = ethWallet.getCryptoForFiat(app, val);
        Assert.assertTrue(res.compareTo(new BigDecimal("3")) == 0);


        //TEST erc20
        WalletTokenManager tokenManager = WalletTokenManager.getBrdWallet(ethWallet);
        Assert.assertNotNull(tokenManager);

        //getCryptoForSmallestCrypto(..)
        val = new BigDecimal("25");
        res = tokenManager.getCryptoForSmallestCrypto(app, val);
        Assert.assertTrue(res.compareTo(new BigDecimal(25)) == 0);

        //getSmallestCryptoForCrypto(..)
        val = new BigDecimal(25);
        res = tokenManager.getSmallestCryptoForCrypto(app, val);
        Assert.assertTrue(res.toPlainString().compareTo(new BigDecimal("25").toPlainString()) == 0);

        //getFiatForSmallestCrypto(..)
        val = new BigDecimal("2");
        res = tokenManager.getFiatForSmallestCrypto(app, val, null);
        Assert.assertEquals(res.doubleValue(), sumRate * brdRate * 2, 0.001); //dollars

        //getSmallestCryptoForFiat(..)
        val = new BigDecimal(3);//
        res = tokenManager.getSmallestCryptoForFiat(app, val);
        Assert.assertEquals(res.doubleValue(), 5, 0.00001);

        //getCryptoForFiat(..)
        val = new BigDecimal(3);//
        res = tokenManager.getCryptoForFiat(app, val);
        Assert.assertEquals(res.doubleValue(), 5, 0.00001);

    }

    @Test
    public void walletPaperKeyTests() {

    }

    @Test
    public void httpTests() {
        final AtomicInteger count = new AtomicInteger();
        final String contract = "0x558ec3152e2eb2174905cd19aea4e34a23de9ad6";
        for (int i = 0; i < 10000; i++) {
            BRExecutor.getInstance().forLightWeightBackgroundTasks().execute(new Runnable() {
                @Override
                public void run() {
                    count.incrementAndGet();
                    final String host = "https://" + BreadApp.HOST + JsonRpcConstants.BRD_ETH_TX_ENDPOINT + "query?";
                    final String eth_rpc_url = host + "module=logs&action=getLogs" +
                            "&fromBlock=0&toBlock=latest" +
//                         "&address=" + ... not needed since we're asking for all the contracts
                            "&topic0=" + "" +
                            "&topic1=" + contract +
                            "&topic1_2_opr=or" +
                            "&topic2=" + contract;
                    Log.d(TAG, "run: " + eth_rpc_url);
                    final JSONObject payload = new JSONObject();
                    try {
                        payload.put("id", String.valueOf(""));
                        // ?? payload.put("account", address);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonRpcRequest.makeRpcRequest(mActivityRule.getActivity(), eth_rpc_url, payload, new JsonRpcRequest.JsonRpcRequestListener() {
                        @Override
                        public void onRpcRequestCompleted(String jsonResult) {

                            Log.e(TAG, "onRpcRequestCompleted: " + jsonResult);

                        }
                    });
                }
            });
        }
        while(count.get() < 10000){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "httpTests: DONE, with count: " + count.get());
    }

}
