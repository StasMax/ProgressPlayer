package com.example.android.progressplayer.ui

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.android.progressplayer.LEFT
import com.example.android.progressplayer.R
import com.example.android.progressplayer.RIGHT
import com.example.android.progressplayer.presenter.MainPresenter
import com.example.android.progressplayer.view.MainView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSeekBar()
        initButtons()
        initFrames()
        mainPresenter.initTrackList()
    }

    private fun initSeekBar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mainPresenter.initChangeProgressOnTouch(seekBar?.progress)
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }
        })
    }

    private fun initFrames() {

        val gestureDoubleTapR = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                mainPresenter.initNextTrack()
                return super.onDoubleTap(e)
            }
        })

        rightFrame.setOnTouchListener { _, event -> gestureDoubleTapR.onTouchEvent(event) }

        rightFrame.setOnLongClickListener {
            mainPresenter.initLongPress(RIGHT)
            true
        }

        val gestureDoubleTapL = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                mainPresenter.initPreviewTrack()
                return super.onDoubleTap(e)
            }
        })

        leftFrame.setOnTouchListener { _, event -> gestureDoubleTapL.onTouchEvent(event) }

        leftFrame.setOnLongClickListener {
            mainPresenter.initLongPress(LEFT)
            true
        }
    }

    override fun moveProgress(progress: Int) {
        seekBar.incrementProgressBy(progress)
    }

    override fun setProgressSeek(progress: Int) {
        seekBar.progress = progress
    }

    override fun setMaxProgress(maxProgress: Int) {
        seekBar.max = maxProgress
    }

    override fun onStartTrack(trackName: String, trackAuthor: String) {
        val txt = "$trackAuthor - $trackName"
        trackSubscription.text = txt
        trackSubscription.visibility = View.VISIBLE
    }

    override fun onStopTrack() {
        trackSubscription.visibility = View.GONE
    }

    private fun initButtons() {
        buttonNext.setOnClickListener { mainPresenter.initNextTrack() }
        buttonPause.setOnClickListener { mainPresenter.initPauseProgress() }
        buttonPreview.setOnClickListener { mainPresenter.initPreviewTrack() }
        buttonStart.setOnClickListener { mainPresenter.initStartProgress(seekBar.progress.toLong()) }
        buttonStop.setOnClickListener { mainPresenter.initStopProgress() }
    }
}
