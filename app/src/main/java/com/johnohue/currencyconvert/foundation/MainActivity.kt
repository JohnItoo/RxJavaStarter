package com.johnohue.currencyconvert.foundation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.afollestad.materialdialogs.MaterialDialog
import com.johnohue.currencyconvert.R
import com.johnohue.currencyconvert.list.LoadingCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loading_indicator.*

class MainActivity : AppCompatActivity(), LoadingCallback {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
//        navController = findNavController(R.id.nav_host_fragment)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
    }

    fun setUpToolBar(toolbarTitle: String, isRootPage: Boolean = false) {
        supportActionBar!!.run {
            setDisplayHomeAsUpEnabled(!isRootPage)
            setHomeAsUpIndicator(if (!isRootPage) R.drawable.ic_arrow_back_white_24dp else 0)
            toolbar.title = toolbarTitle
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun showLoading() {
        showLoading(R.string.default_loading_message)
    }

    override fun showLoading(resId: Int) {
        showLoading(getString(resId))
    }

    override fun showLoading(message: String) {
        hideKeyBoard()
        progressMessage.text = message
        loading_layout_container.showViewWithChildren()
        disableTouch()
    }

    override fun dismissLoading() {
        loading_layout_container.hide()
        enableTouch()
    }

    override fun showError(resId: Int) {
        showError(getString(resId))
    }

    override fun showError(message: String) {
        hideKeyBoard()
        dismissLoading()
        MaterialDialog(this).show {
            title(text = message).cancelable(false)
        }
    }
}