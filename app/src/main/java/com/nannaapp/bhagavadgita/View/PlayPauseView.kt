package com.nannaapp.bhagavadgita.View

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AttrRes
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.nannaapp.bhagavadgita.R


class PlayPauseView : AppCompatImageView {
    private var mPlayToPauseAnim: AnimatedVectorDrawableCompat? = null
    private var mPauseToPlay: AnimatedVectorDrawableCompat? = null
    private var mFadeOutAnim: Animation? = null
    private var mFadeInAnim: Animation? = null

    constructor(context: Context) : super(context) {
        Init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        Init(context)
    }

    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        Init(context)
    }

    private fun Init(context: Context) {
        mPlayToPauseAnim = AnimatedVectorDrawableCompat.create(context, R.drawable.play_to_pause)
        mPauseToPlay = AnimatedVectorDrawableCompat.create(context, R.drawable.pause_to_play)
        mFadeOutAnim = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        mFadeInAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    }

    fun setState(state: Int) {
        when (state) {
            STATE_PLAY -> {
                setImageDrawable(mPlayToPauseAnim)
                mPlayToPauseAnim!!.start()
            }
            STATE_PAUSE -> {
                setImageDrawable(mPauseToPlay)
                mPauseToPlay!!.start()
            }
        }
    }

    fun fadeOut() {
        startAnimation(mFadeOutAnim)
        mFadeOutAnim?.setFillAfter(true)
    }

    fun fadeIn() {
        startAnimation(mFadeInAnim)
        mFadeInAnim?.setFillAfter(true)
    }

    companion object {
        const val STATE_PLAY = 1
        const val STATE_PAUSE = 2
    }
}