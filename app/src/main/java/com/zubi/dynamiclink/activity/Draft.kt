package com.zubi.dynamiclink.activity

import android.content.ActivityNotFoundException
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


class Draft : AppCompatActivity() {

    private lateinit var campaignAdapter: CampaignAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var campaignList: MutableList<CampaignData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initDynamicLink()

        viewManager = LinearLayoutManager(this)
        campaignList = mutableListOf()

        campaignList.add(
            CampaignData(
                "https://r10.page.link/rakuten",
                "Rakuten",
                "This is the description for Rakuten campaign",
                "https://d2q79iu7y748jz.cloudfront.net/s/_squarelogo/f0f13eecfafde7eab61e138873da8cda"
            )
        )
        campaignList.add(
            CampaignData(
                "https://play.google.com/store/apps/details?id=com.viber.voip&hl=en&link=viber://forward?text%3Dfoo",
                "Viber",
                "This is the description for Viber campaign",
                "https://cnet1.cbsistatic.com/img/2014/04/24/cd83132a-a52f-49ad-a9f7-0f8b7514960a/icon.png"
            )
        )
        //campaignList.add(CampaignData("https://play.google.com/store/apps/details?id=com.viber.voip&hl=en&link=https%3A%2F%2Fline.me%2FR%2Fnv%2FrecommendOA%2F%40linedevelopers", "Line", "This is the description for Line campaign", "https://pht.qoo-static.com/74iMObG1vsR3Kfm82RjERFhf99QFMNIY211oMvN636_gULghbRBMjpVFTjOK36oxCbs=w512"))
        campaignList.add(
            CampaignData(
                "https://play.google.com/store/apps/details?id=com.viber.voip&hl=en&link=line%3A%2F%2F",
                "Line",
                "This is the description for Line campaign",
                "https://pht.qoo-static.com/74iMObG1vsR3Kfm82RjERFhf99QFMNIY211oMvN636_gULghbRBMjpVFTjOK36oxCbs=w512"
            )
        )

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
                        /*val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(campaignList[position].redirectUrl))
                        startActivity(browserIntent)*/
                        var deepLink: String = campaignList[position].redirectUrl
                        var link = Uri.parse(deepLink).getQueryParameter("link")

                        if (deepLink.toString().startsWith("https://play.google.com/store/apps")
                            && link.toString().isNotBlank()
                        ) {
                            try {
                                openRakutenCardApp(link.toString())
                            } catch (ex: ActivityNotFoundException) {
                                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))
                                startActivity(intent)
                            }
                        }
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
                var link: String = ""
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    packageId = deepLink?.getQueryParameter("id").toString()
                    link = deepLink?.getQueryParameter("link").toString()

                }

                if (deepLink.toString()
                        .startsWith("https://play.google.com/store/apps") && link.isNotBlank()
                ) {

                    val packageName = packageId
                    var intent = packageManager.getLaunchIntentForPackage(packageName)

                    if (intent == null) {
                        intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$packageName")
                        )
                    }
                    startActivity(intent)
                }
            }
            .addOnFailureListener(this) { e -> Log.w("~ ", "getDynamicLink:onFailure", e) }
    }

    /**
     * Opens Rakuten Card App by deeplink
     */
    @Throws(ActivityNotFoundException::class)
    private fun openRakutenCardApp(cardURI: String) {
        // Define Intent
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(cardURI));
        // Start Activity
        startActivity(intent);
    }
}
