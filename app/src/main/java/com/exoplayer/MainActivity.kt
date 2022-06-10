package com.exoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter

class MainActivity : AppCompatActivity(), Player.Listener {

    lateinit var playerView: PlayerView
    lateinit var player: ExoPlayer
    lateinit var progressBar: ProgressBar
    lateinit var tv_title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        tv_title = findViewById(R.id.tv_title)
        initViews()
        addMp4Files()
        addMp4Files()

        if (savedInstanceState != null) {
            savedInstanceState.getInt("mediaItem").let { restoredMedia ->
                val seekTime = savedInstanceState.getLong("SeekTime")
                player.seekTo(restoredMedia, seekTime)
                player.play()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("SeekTime", player.currentPosition)
        outState.putInt("mediaItem", player.currentMediaItemIndex)
    }

    private fun initViews() {
        player = ExoPlayer.Builder(this).build()
        playerView = findViewById(R.id.exoPlayer)
        playerView.player = player
        player.addListener(this)


    }

    private fun addMp4Files() {
        val mediaItem = MediaItem.fromUri("https://assets.mixkit.co/videos/preview/mixkit-people-pouring-a-warm-drink-around-a-campfire-513-large.mp4")
        player.addMediaItem(mediaItem)
        player.prepare()
    }

    private fun addMp3Files() {
        val mediaItem = MediaItem.fromUri("mp3")
        player.addMediaItem(mediaItem)
        player.prepare()
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)

        when (playbackState) {
            Player.STATE_BUFFERING -> {
                progressBar.visibility = View.VISIBLE
            }
            Player.STATE_READY -> {
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)
        tv_title.text = mediaMetadata.title ?: mediaMetadata.displayTitle ?: "title not found"
    }


}