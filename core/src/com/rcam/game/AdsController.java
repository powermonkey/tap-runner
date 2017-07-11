package com.rcam.game;

/**
 * Created by Rod on 6/25/2017.
 */

public interface AdsController {
    boolean isWifiConnected();
    void showBannerAd();
    void hideBannerAd();
    void showInterstitialAd(Runnable then);
}
