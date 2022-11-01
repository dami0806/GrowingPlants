package com.dami.growingplants

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "bcf5c437937b360d698fda2fb6668741")
    }
}