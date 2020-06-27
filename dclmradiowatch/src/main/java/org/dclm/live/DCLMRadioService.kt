package org.dclm.live

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.widget.MediaController
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.media.session.MediaButtonReceiver
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util


class DCLMRadioService : Service(), MediaController.MediaPlayerControl {
    private val binder = RadioLocalBinder()

    private lateinit var playerNotificationManager: PlayerNotificationManager
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var context: Context
    private  var check = false
    lateinit var mediaSource: MediaSource
    private lateinit var preferences: SharedPreferences
    val PREFRENCES = "org.dclm.radio"
    val LINK = "LINK"
    var position = 0
    var currentPosition: Long = 0
    var destroyed = false
    lateinit var player: SimpleExoPlayer
    val playState: MutableLiveData<Boolean> = MutableLiveData()
    var buttonState = false

    inner class RadioLocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods

        fun getService() : DCLMRadioService{
            return this@DCLMRadioService
        }

    }
    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        destroyed = false
        context = this
        preferences = applicationContext.getSharedPreferences(PREFRENCES, Context.MODE_PRIVATE)
        val audioAttributes = AudioAttributes.Builder().setUsage(com.google.android.exoplayer2.C.USAGE_MEDIA).setContentType(
            com.google.android.exoplayer2.C.CONTENT_TYPE_SPEECH).build()

        player = SimpleExoPlayer.Builder(this).build()

        player.setAudioAttributes(audioAttributes, true)
        player.addListener(object : Player.EventListener {

            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                buttonState = if (!playWhenReady) {
                    stopForeground(false)
                    playState.postValue(false)
                    false

                } else {
                    startForeground()
                    playState.postValue(true)
                    true
                }
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                Toast.makeText(context, getString(R.string.no_connection), Toast.LENGTH_LONG).show()
                playState.postValue(false)
                buttonState = false
            }

            override fun onPositionDiscontinuity(reason: Int) {
                val latestWindowIndex = player.currentWindowIndex
                if (latestWindowIndex != position) {
                    // item selected in playlist has changed, handle here
                    position = latestWindowIndex
                }
            }
        })
        mediaSession = MediaSessionCompat(context, C.MEDIA_SESSION_TAG)
        startForeground()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        check = true
        mediaSource = ProgressiveMediaSource.Factory(
            DefaultHttpDataSourceFactory(
                Util.getUserAgent(context, getString(R.string.app_name)),
                null /* listener */,
                30 * 1000,
                30 * 1000,
                true ) //allowCrossProtocolRedirects
        ).createMediaSource(Uri.parse(preferences.getString(LINK, getString(R.string.radio_link))))
        player.prepare(mediaSource)
        player.seekTo(position, currentPosition)
        player.playWhenReady = true
        startForeground()
        MediaButtonReceiver.handleIntent(mediaSession, intent)
        return START_STICKY
    }

    private fun startForeground() {

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(context, C.PLAYBACK_CHANNEL_ID, R.string.app_name, R.string.app_describe,
            C.PLAYBACK_NOTIFICATION_ID,
            object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): String {
                    return getString(R.string.app_name)
                }

                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    Intent(context, MainActivity::class.java).apply {
                        this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        return PendingIntent.getActivity(context, 0, this, 0)
                    }

                }

                override fun getCurrentContentText(player: Player): String? {
                    return getString(R.string.app_describe)
                }

                override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
                    return getBitmap(context, R.drawable.nlogo2)
                }
            },
            object : PlayerNotificationManager.NotificationListener {
                @RequiresApi(api = Build.VERSION_CODES.N)
                override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                    if (dismissedByUser) {
                        stopSelf()
                    }
                    stopSelf()
                    // stopForeground(STOP_FOREGROUND_REMOVE);
                }

                override fun onNotificationPosted(notificationId: Int, notification: Notification, ongoing: Boolean) {
                    if (ongoing) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForeground(notificationId, notification)
                        }
                    } else {
                        stopForeground(false)
                    }
                }
            })
        playerNotificationManager.setSmallIcon(R.drawable.nlogo2)
        playerNotificationManager.setUseStopAction(false)
        playerNotificationManager.setFastForwardIncrementMs(2)
        playerNotificationManager.setRewindIncrementMs(2)
        playerNotificationManager.setUseNavigationActions(true)
        playerNotificationManager.setPlayer(player)
        mediaSession.isActive = true
        playerNotificationManager.setMediaSessionToken(mediaSession.sessionToken)
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(player)
    }
    fun getBitmap(context: Context, @DrawableRes bitmapResource: Int): Bitmap? {
        return (context.resources.getDrawable(bitmapResource, null) as BitmapDrawable).bitmap
    }

    override fun onDestroy() {
        destroyed = true
        position = player.currentWindowIndex
        currentPosition = player.contentPosition
        if (check) {
            mediaSession.release()
            mediaSessionConnector.setPlayer(null)
            playerNotificationManager.setPlayer(null)
            player.release()
        }

        super.onDestroy()
    }

    override fun isPlaying(): Boolean {
        return this.player.isPlaying
    }

    override fun canSeekForward(): Boolean {
        return false
    }

    override fun getDuration(): Int {
        return player.duration.toInt()
    }

    override fun pause() {
        pausePlayer()
    }

    override fun getBufferPercentage(): Int {
        return this.player.bufferedPercentage
    }

    override fun seekTo(pos: Int) {
        player.seekTo(pos.toLong())
    }

    override fun getCurrentPosition(): Int {
        return this.player.currentPosition.toInt()
    }

    override fun canSeekBackward(): Boolean {
        return false
    }

    override fun start() {
        startPlayer()
    }

    override fun getAudioSessionId(): Int {
        return this.player.audioSessionId
    }

    override fun canPause(): Boolean {
        return true
    }

    fun pausePlayer() {
        if (player.playWhenReady == player.isPlaying){
            player.playWhenReady = false
        }

    }

    fun startPlayer() {
        if (player.playWhenReady != player.isPlaying){
            player.playWhenReady = true
        }
    }
}