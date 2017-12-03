package com.rcam.game;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AndroidLauncher extends AndroidApplication implements AdsController{
	private static final String BANNER_AD_UNIT_ID = "ca-app-pub-5225464865745943/1272805115";
//	private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";


	AdView bannerAd;
//	InterstitialAd interstitialAd;
	View gameView;
	RelativeLayout layout;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		gameView = initializeForView(new TapRunner(this), config);
		setupAds();
		layout = new RelativeLayout(this);
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layout.addView(bannerAd, params);

		setContentView(layout);
	}

	public void setupAds() {
		bannerAd = new AdView(this);
		bannerAd.setVisibility(View.INVISIBLE);
		bannerAd.setBackgroundColor(Color.TRANSPARENT);
		bannerAd.setAdUnitId(BANNER_AD_UNIT_ID);
		bannerAd.setAdSize(AdSize.SMART_BANNER);

//		interstitialAd = new InterstitialAd(this);
//		interstitialAd.setAdUnitId(INTERSTITIAL_AD_UNIT_ID);
//
//		AdRequest.Builder builder = new AdRequest.Builder();
//		AdRequest ad = builder.build();
//		interstitialAd.loadAd(ad);
	}

	@Override
	public boolean isWifiConnected() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
		return isConnected;
	}

	@Override
	public void showBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bannerAd.setVisibility(View.VISIBLE);
				AdRequest.Builder builder = new AdRequest.Builder();
				AdRequest ad = builder.build();
				bannerAd.loadAd(ad);
			}
		});
	}

	@Override
	public void hideBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bannerAd.setVisibility(View.INVISIBLE);
			}
		});
	}

//	@Override
//	public void showInterstitialAd(final Runnable then) {
//		runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				if(interstitialAd != null && interstitialAd.isLoaded()) {
//					interstitialAd.show();
////					if (then != null) {
//						interstitialAd.setAdListener(new AdListener() {
//							@Override
//							public void onAdClosed() {
//								AdRequest.Builder builder = new AdRequest.Builder();
//								AdRequest ad = builder.build();
//								interstitialAd.loadAd(ad);
//							}
//						});
////					}
//				}
//			}
//		});
//	}

	@Override
	public void onDestroy() {
		if (bannerAd != null) {
			bannerAd.destroy();
		}
		super.onDestroy();
	}
}
