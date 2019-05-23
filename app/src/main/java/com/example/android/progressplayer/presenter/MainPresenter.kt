package com.example.android.progressplayer.presenter

import android.view.MotionEvent
import android.widget.SeekBar
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.android.progressplayer.*
import com.example.android.progressplayer.view.MainView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    private val disposeBag = CompositeDisposable()
    private var duration: Long = 0
    private var startTime: Long = 0

    fun initPauseProgress() {
        disposeBag.clear()
    }

    fun initNextProgress() {
        viewState.moveProgress(STEP_MOVE_UP)
    }

    fun initPreviewProgress() {
        viewState.moveProgress(STEP_MOVE_DOWN)
    }

    fun initStartProgress(progress: Long) {
        disposeBag.add(timing()
            .take(MAX_SEEK_BAR - progress)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewState.moveProgress(STEP_SEEK_BAR) }
        )
    }

    fun initStopProgress() {
        disposeBag.clear()
        viewState.setProgressSeek(0)
    }

    private fun timing(): Observable<Long> {
        return Observable.interval(1000, TimeUnit.MILLISECONDS)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
    }

    fun initChangeProgress(seekBar: SeekBar?) {
        seekBar?.let {
            initPauseProgress()
            initStartProgress(seekBar.progress.toLong())
        }
    }

    fun initRightFrameTouch(v: MotionEvent?): Boolean {
        return initDoubleClick(v, RIGHT)
    }

    fun initLeftFrameTouch(v: MotionEvent?): Boolean {
        return initDoubleClick(v, LEFT)
    }

    private fun initDoubleClick(event: MotionEvent?, side: Int): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            duration = System.currentTimeMillis() - startTime
            when {
                duration > DURATION_DOUBLE_CLICK -> {
                    startTime = System.currentTimeMillis()
                    return false
                }
                side == RIGHT -> initNextProgress()
                side == LEFT -> initPreviewProgress()
            }
            startTime = System.currentTimeMillis()
            return true
        }
        return false
    }
}