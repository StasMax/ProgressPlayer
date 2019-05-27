package com.example.android.progressplayer.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.android.progressplayer.*
import com.example.android.progressplayer.model.TrackExample
import com.example.android.progressplayer.model.TrackModel
import com.example.android.progressplayer.view.MainView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    private val disposeBag = CompositeDisposable()
    private val disposeTrack = CompositeDisposable()
    val trackExample = TrackExample()
    val tracklist = arrayListOf<TrackModel>()

    lateinit var playTrack: TrackModel
    var countTrack = 0

    fun initPauseProgress() {
        disposeTrack.clear()
    }

    fun initNextTrack() {
        if (countTrack != tracklist.size - 1) {
            countTrack++
            playTrack = tracklist[countTrack]
            initStopProgress()
            initStartProgress(0)
        }
    }

    fun initPreviewTrack() {
        if (countTrack != 0) {
            countTrack--
            playTrack = tracklist[countTrack]
            initStopProgress()
            initStartProgress(0)
        }
    }

    fun initStartProgress(progress: Long) {
        viewState.onStartTrack(playTrack.trackName, playTrack.trackAutor)
        viewState.setMaxProgress(playTrack.trackTime)
        timing()
            .take(playTrack.trackTime - progress)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { initNextTrack() }
            .subscribe { viewState.moveProgress(STEP_SEEK_BAR) }
            .let { disposeTrack.add(it) }
    }

    fun initStopProgress() {
        initPauseProgress()
        viewState.setProgressSeek(0)
        viewState.onStopTrack()
    }

    private fun timing(): Observable<Long> {
        return Observable.interval(1, TimeUnit.SECONDS)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        disposeTrack.clear()
    }

    fun initChangeProgressOnTouch(seekBar: Int?) {
        seekBar?.let {
            initPauseProgress()
            initStartProgress(seekBar.toLong())
        }
    }

    fun initTrackList() {
        trackExample.getTrackList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { playTrack = tracklist[countTrack] }
            .subscribe { tracklist.add(it) }
            .let { disposeBag.add(it) }
    }

    fun initLongPress(side: Int) {
        when (side) {
            RIGHT -> viewState.moveProgress(STEP_MOVE_UP)
            LEFT -> viewState.moveProgress(STEP_MOVE_DOWN)
        }
    }
}