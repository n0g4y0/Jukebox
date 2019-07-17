package com.nogas.jukebox

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class JukeboxService : Service() {

    companion object {
        const val PLAY_ACTION = "Play"
        const val PLAY_INDEX_ACTION = "PlayIndex"
        const val NEXT_TRACK_ACTION = "Next"
        const val STOP_ACTION = "Stop"

    }

    private lateinit var songs: Array<String>           // list of all songs
    private var media : MediaPlayer? = null             // media player for current song
    private var index = -1                              // current song index being played

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        songs = resources.getStringArray(R.array.song_titles)

        if (intent?.action == JukeboxService.PLAY_ACTION){
            playNextSong()

        }else if (intent?.action == JukeboxService.PLAY_INDEX_ACTION){
            val index = intent!!.getIntExtra("index",0)
            playSong(index)
        }else if (intent?.action == JukeboxService.NEXT_TRACK_ACTION){
            playNextSong()
        }else if (intent?.action == JukeboxService.STOP_ACTION){
            stopSong()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    //Plays the track at the given index.
    //Stop playing any other track that was playing.
    //Also sets this index to be the new current index.

    private fun playSong(index:Int){
        stopSong()

        // get resource ID, example: "E Honda" -> R.raw.ehonda
        this.index = index % songs.size
        val songName = songs[this.index].toLowerCase().replace(" ","")
        val resID = resources.getIdentifier(songName,"raw",packageName)
        if (resID <= 0) return

        media = MediaPlayer.create(this,resID)
        if (media == null)  return
        media?.setOnCompletionListener {
            onSongComplete()
        }
        media?.start()

    }

    //  Moves to the next track, then starts playing it
    private fun playNextSong(){
        index = (index +1) % songs.size
        playSong(index)
    }

    private fun onSongComplete(){
        //  go to the next song, if still playing
        if (media != null){
            playNextSong()
        }
    }

    //  stop playing any song that is currently playing.
    private fun stopSong(){
        if (media !=null){
            media?.stop()
            media = null
        }
    }
}
