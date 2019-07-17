package com.nogas.jukebox

import android.content.Intent
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_jukebox.*

class JukeboxActivity : AppCompatActivity() {



    //  Initializes the activity when it is created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jukebox)



        song_list.setOnItemClickListener { _, _, index, _ ->
            playSongAtIndex(index)
        }
    }

    private fun doIntent(action:String){
        val intent = Intent(this,JukeboxService::class.java)
        intent.action = action
        startService(intent)
    }

    fun playSongAtIndex(index:Int){
        val intent = Intent(this,JukeboxService::class.java)
        intent.action = JukeboxService.PLAY_INDEX_ACTION
        intent.putExtra("index",index)
        startService(intent)

    }

    //  plays the song at the current index
    fun onCLickPlay(view:View){
        //playSong(index)
        // tell the service to play the song
        doIntent(JukeboxService.PLAY_ACTION)
    }

    //  goes to the next track
    fun onClickNextTrack(view:View){
        //playNextSong()
        doIntent(JukeboxService.NEXT_TRACK_ACTION)
    }

    fun onClickStop(view:View){
        //stopSong()
        doIntent(JukeboxService.STOP_ACTION)
    }



    // The methods below do the actual work of playing the songs

    //Plays the track at the given index.
    //Stop playing any other track that was playing.
    //Also sets this index to be the new current index.
}
