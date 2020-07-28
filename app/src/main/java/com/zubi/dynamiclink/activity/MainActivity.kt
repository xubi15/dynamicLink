package com.zubi.dynamiclink.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.zubi.dynamiclink.R
import com.zubi.dynamiclink.adapter.CampaignAdapter
import com.zubi.dynamiclink.interfaces.ClickListener
import com.zubi.dynamiclink.model.CampaignData
import com.zubi.dynamiclink.util.RecyclerClick
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var campaignAdapter: CampaignAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var campaignList: MutableList<CampaignData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDynamicLink()

        viewManager = LinearLayoutManager(this)
        campaignList = mutableListOf()

        campaignList.add(CampaignData("https://r10.page.link/rakuten", "Rakuten", "This is the description for Rakuten campaign", "https://d2q79iu7y748jz.cloudfront.net/s/_squarelogo/f0f13eecfafde7eab61e138873da8cda"))
        campaignList.add(CampaignData("https://r10.page.link/viber", "Viber", "This is the description for Viber campaign", "https://cnet1.cbsistatic.com/img/2014/04/24/cd83132a-a52f-49ad-a9f7-0f8b7514960a/icon.png"))
        campaignList.add(CampaignData("https://r10.page.link/line", "Line", "This is the description for Line campaign", "https://pht.qoo-static.com/74iMObG1vsR3Kfm82RjERFhf99QFMNIY211oMvN636_gULghbRBMjpVFTjOK36oxCbs=w512"))

        campaignAdapter = CampaignAdapter(campaignList, applicationContext)
        recyclerView.apply {
            adapter = campaignAdapter
            layoutManager = viewManager
        }

        recyclerView!!.addOnItemTouchListener(
                RecyclerClick(
                        applicationContext!!,
                        recyclerView!!,
                        object : ClickListener {

                            override fun onClick(view: View?, position: Int) {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(campaignList[position].redirectUrl))
                                startActivity(browserIntent)
                            }

                            override fun onLongClick(view: View?, position: Int) {}
                        }
                )
        )

    }

    private fun initDynamicLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    // Get deep link from result (may be null if no link is found)
                    var deepLink: Uri? = null
                    var packageId: String = ""
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.link
                        packageId = deepLink?.getQueryParameter("id").toString()
                    }
                    //println("~ $packageId")
                    if (packageId != "") {
                        val packageName = packageId
                        //println("!~: $packageName")
                        var intent = packageManager.getLaunchIntentForPackage(packageName)

                        if (intent == null) {
                            intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                        }

                        startActivity(intent)
                    }
                }
                .addOnFailureListener(this) { e -> Log.w("~ ", "getDynamicLink:onFailure", e) }

    }
}
