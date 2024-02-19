package com.teovladusic.skratchassignment

import android.app.Application
import com.mapbox.common.MapboxOptions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SkratchAssignmentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MapboxOptions.accessToken = this.getString(R.string.mapbox_access_token)
    }
}