package net.softglobe.adstutorial

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class MainActivity : AppCompatActivity() {
    private val TAG = "checkAdImplementation"
    private var mInterstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this)

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        findViewById<AdView>(R.id.banner_ad_layout).loadAd(adRequest)

        loadInterstitialAd(adRequest)

        loadRewardedAd(adRequest)

        findViewById<Button>(R.id.btn_load_interstitial_ad).setOnClickListener {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
                loadInterstitialAd(adRequest)
            } else {
                Log.d(TAG, "The interstitial ad wasn't ready yet.")
            }
        }

        findViewById<Button>(R.id.btn_load_rewarded_ad).setOnClickListener {
            rewardedAd?.let { ad ->
                ad.show(this) { rewardItem ->
                    // Handle the reward.
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    Log.d(TAG, "User earned the reward.")
                }
            } ?: run {
                Log.d(TAG, "The rewarded ad wasn't ready yet.")
            }
            loadRewardedAd(adRequest)
        }
    }

    private fun loadRewardedAd(adRequest: AdRequest) {
        RewardedAd.load(
            this,
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, "Error: $adError")
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    rewardedAd = ad
                }
            })
    }

    private fun loadInterstitialAd(adRequest: AdRequest) {
        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, "Error: $adError")
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })
    }
}