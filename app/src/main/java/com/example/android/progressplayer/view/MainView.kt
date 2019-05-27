package com.example.android.progressplayer.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun moveProgress(progress: Int)
    fun setProgressSeek(progress: Int)
    fun setMaxProgress(maxProgress: Int)
    fun onStartTrack(trackName: String, trackAuthor: String)
    fun onStopTrack()

}