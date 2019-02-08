package eu.morningbird.czyhandlowa.view

/*
 *  This file is part of "Czy handlowa?" android application.
 *
 *  "Czy handlowa?" is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  "Czy handlowa?" is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with "Czy handlowa?". If not, see <http://www.gnu.org/licenses/>.
*/

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import eu.morningbird.czyhandlowa.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.util.*


class AboutActivity : AppCompatActivity() {

    private var firebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val aboutPage = AboutPage(this)
            .setDescription(getString(R.string.about_app_description))
            .isRTL(false)
            .setImage(R.drawable.ic_crab)
            .addItem(getVersionElement())
            .addGroup(getString(R.string.about_contact_me))
            .addEmail("contact@morningbird.eu")
            .addItem(getPersonalWebsiteElement())
            .addGroup(getString(R.string.about_find_this_app))
            .addWebsite("https://morningbird.eu")
            .addPlayStore("eu.morningbird.czyhandlowa")
            .addGitHub("michaldaniel")
            .addGroup(getString(R.string.about_support_suggestions_bug_reports))
            .addItem(getSupportElement())
            .addGroup(getString(R.string.about_privacy))
            .addItem(getPrivacyPolicyElement())
            .addGroup(getString(R.string.about_copyright_and_license))
            .addItem(getCopyRightsElement())
            .addItem(getLicenseElement())
            .addItem(getSourceCodeElement())
            .addGroup(getString(R.string.about_attributions))
            .addItem(getAttributionElement())

            .create()

        setContentView(aboutPage)
    }

    private fun getAttributionElement(): Element {
        val element = Element()
        element.title = getString(R.string.about_attribution_app_icon)
        element.iconDrawable = R.drawable.ic_account_group
        element.iconTint = mehdi.sakout.aboutpage.R.color.about_item_icon_color
        element.onClickListener = View.OnClickListener {
            val url = "https://www.freepik.com"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "attribution")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "attribution")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "about link")
            firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
        return element
    }

    private fun getCopyRightsElement(): Element {
        val element = Element()
        val copyrights = getString(R.string.about_copyright, Calendar.getInstance().get(Calendar.YEAR))
        element.title = copyrights
        element.iconDrawable = R.drawable.ic_copyright
        element.iconTint = mehdi.sakout.aboutpage.R.color.about_item_icon_color
        element.onClickListener = View.OnClickListener {
            val url = "https://github.com/michaldaniel/czyhandlowa-android/blob/master/NOTICE"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "copyrights")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "copyrights")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "about link")
            firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
        return element
    }

    private fun getLicenseElement(): Element {
        val element = Element()
        element.title = getString(R.string.about_released_under_the_gnu_gpl)
        element.iconDrawable = R.drawable.ic_file_document
        element.iconTint = mehdi.sakout.aboutpage.R.color.about_item_icon_color
        element.onClickListener = View.OnClickListener {
            val url = "https://github.com/michaldaniel/czyhandlowa-android/blob/master/LICENSE"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "license")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "license")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "about link")
            firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
        return element
    }

    private fun getPersonalWebsiteElement(): Element {
        val element = Element()
        element.title = getString(R.string.about_personal_website)
        element.iconDrawable = R.drawable.ic_link
        element.iconTint = mehdi.sakout.aboutpage.R.color.about_item_icon_color
        element.onClickListener = View.OnClickListener {
            val url = "https://morningbird.eu"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "personal_website")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "personal website")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "about link")
            firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
        return element
    }

    private fun getPrivacyPolicyElement(): Element {
        val element = Element()
        element.title = getString(R.string.about_read_privacy_policy)
        element.iconDrawable = R.drawable.ic_eye
        element.iconTint = mehdi.sakout.aboutpage.R.color.about_item_icon_color
        element.onClickListener = View.OnClickListener {
            val url = "https://morningbird.eu/app/czyhandlowa/privacy"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "privacy_policy")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "privacy policy")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "about link")
            firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
        return element
    }

    private fun getSourceCodeElement(): Element {
        val element = Element()
        element.title = getString(R.string.about_get_the_source_code)
        element.iconDrawable = R.drawable.ic_source_fork
        element.iconTint = mehdi.sakout.aboutpage.R.color.about_item_icon_color
        element.onClickListener = View.OnClickListener {
            val url = "https://github.com/michaldaniel/czyhandlowa-android"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "source_code")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "source code")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "about link")
            firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
        return element
    }

    private fun getSupportElement(): Element {
        val element = Element()
        element.title = getString(R.string.about_file_bug_report)

        element.iconDrawable = R.drawable.ic_bug
        element.iconTint = mehdi.sakout.aboutpage.R.color.about_item_icon_color
        element.onClickListener = View.OnClickListener {
            val url = "https://github.com/michaldaniel/czyhandlowa-android/issues/new"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "support")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "support")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "about link")
            firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
        return element
    }

    private fun getVersionElement(): Element {
        val element = Element()
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            element.title = getString(R.string.about_version_no, version)
        } catch (e: PackageManager.NameNotFoundException) {
            element.title = getString(R.string.about_version_unknown)
            e.printStackTrace()
        }

        element.iconDrawable = R.drawable.ic_android
        element.iconTint = mehdi.sakout.aboutpage.R.color.about_item_icon_color
        element.onClickListener = View.OnClickListener {
            val url = "https://play.google.com/store/apps/details?id=eu.morningbird.czyhandlowa"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "version")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "version")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "about link")
            firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
        return element
    }

}



