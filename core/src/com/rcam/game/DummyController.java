package com.rcam.game;

/**
 * Created by Rod on 6/25/2017.
 */

class DummyController implements AdsController {
    @Override
    public boolean isWifiConnected() {
        return false;
    }

    @Override
    public void showBannerAd() {

    }

    @Override
    public void hideBannerAd() {

    }

    @Override
    public void showInterstitialAd(Runnable then) {

    }
}
