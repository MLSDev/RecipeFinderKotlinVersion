package com.mlsdev.recipefinder.kotlinversion.view

import android.arch.lifecycle.LifecycleRegistry
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import com.mlsdev.recipefinder.kotlinversion.R
import com.mlsdev.recipefinder.kotlinversion.databinding.ActivityMainBinding
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : BaseActivity() {

    companion object {
        const val LOG_TAG = "RECIPE_FINDER"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var broadcastReceiver: AppBroadcastReceiver
    private val lifecycleRegistry = LifecycleRegistry(this)

    @Inject
    lateinit var bottomNavigationItemSelectedListener: BottomNavigationItemSelectedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        broadcastReceiver = AppBroadcastReceiver()
        initNavigation()
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver, IntentFilter(broadcastReceiver.SHOW_ERROR_ACTION))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(broadcastReceiver)
    }

    private fun initNavigation() {
        lifecycle.addObserver(bottomNavigationItemSelectedListener)
        bottomNavigationItemSelectedListener.currentMenuItem = binding.bnvNavigationView.menu.getItem(0)
        bottomNavigationItemSelectedListener.fragmentManager = supportFragmentManager
        binding.bnvNavigationView.setOnNavigationItemSelectedListener(bottomNavigationItemSelectedListener)
    }

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    inner class AppBroadcastReceiver : BroadcastReceiver() {
        val SHOW_ERROR_ACTION = "show_error"

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SHOW_ERROR_ACTION) {
                val builder = AlertDialog.Builder(context, R.style.AlertDialogAppCompat)

                if (intent.hasExtra(Extras.ALERT_DIALOG_TITLE))
                    builder.setTitle(intent.getStringExtra(Extras.ALERT_DIALOG_TITLE))

                if (intent.hasExtra(Extras.ALERT_DIALOG_MESSAGE))
                    builder.setMessage(intent.getStringExtra(Extras.ALERT_DIALOG_MESSAGE))

                builder.setPositiveButton(android.R.string.ok, null)
                builder.create().show()
            }
        }
    }
}
