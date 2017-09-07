package com.mlsdev.recipefinder.kotlinversion.di.module

import android.content.Context
import com.mlsdev.recipefinder.kotlinversion.view.utils.DiagramUtils
import com.mlsdev.recipefinder.kotlinversion.view.utils.ParamsHelper
import com.mlsdev.recipefinder.kotlinversion.view.utils.UtilsUI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilsModule {

    @Provides
    @Singleton
    fun provideDiagramUtils(utilsUI: UtilsUI): DiagramUtils {
        return DiagramUtils(utilsUI)
    }

    @Provides
    @Singleton
    fun provideUtilsUI(context: Context): UtilsUI {
        return UtilsUI (context)
    }

    @Provides
    @Singleton
    fun provideParamsHelper(): ParamsHelper {
        return ParamsHelper()
    }

}
