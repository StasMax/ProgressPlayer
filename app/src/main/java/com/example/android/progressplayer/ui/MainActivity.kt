package com.example.android.progressplayer.ui

import android.os.Bundle
import android.widget.SeekBar
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.android.progressplayer.AdapterSeekBar
import com.example.android.progressplayer.MAX_SEEK_BAR
import com.example.android.progressplayer.R
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
    }

    private fun initSeekBar() {
        seekBar.max = MAX_SEEK_BAR
        seekBar.setOnSeekBarChangeListener(object : AdapterSeekBar() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mainPresenter.initChangeProgress(seekBar)
            }
        })
    }

    private fun initFrames() {
        rightFrame.setOnTouchListener { _, event -> mainPresenter.initRightFrameTouch(event) }
        leftFrame.setOnTouchListener { _, event -> mainPresenter.initLeftFrameTouch(event) }
    }

    override fun moveProgress(progress: Int) {
        seekBar.incrementProgressBy(progress)
    }

    override fun setProgressSeek(progress: Int) {
        seekBar.progress = progress
    }

    private fun initButtons() {
        buttonNext.setOnClickListener { mainPresenter.initNextProgress() }
        buttonPause.setOnClickListener { mainPresenter.initPauseProgress() }
        buttonPreview.setOnClickListener { mainPresenter.initPreviewProgress() }
        buttonStart.setOnClickListener { mainPresenter.initStartProgress(seekBar.progress.toLong()) }
        buttonStop.setOnClickListener { mainPresenter.initStopProgress() }
    }
}
