package com.nogas.jukebox

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_jukebox.*

class JukeboxActivity : AppCompatActivity() {

    private lateinit var songs: Array<String>           // list of all songs
    private var media : MediaPlayer? = null             // media player for current song
    private var index = -1                              // current song index being played

    //  Initializes the activity when it is created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jukebox)

        songs = resources.getStringArray(R.array.song_titles)

        song_list.setOnItemClickListener { _, _, index, _ ->
            playSong(index)
        }
    }

    ///// Event - Listener methods

    //  plays the song at the current index
    fun onCLickPlay(view:View){
        playSong(index)
    }

    //  goes to the next track
    fun onClickNextTrack(view:View){
        playNextSong()
    }

    fun onClickStop(view:View){
        stopSong()
    }



    // The methods below do the actual work of playing the songs

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
