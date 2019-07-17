package com.nogas.jukebox

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder

class JukeboxService : Service() {

    companion object {
        const val PLAY_ACTION = "Play"
        const val PLAY_INDEX_ACTION = "PlayIndex"
        const val NEXT_TRACK_ACTION = "Next"
        const val STOP_ACTION = "Stop"
        // constante para el ID de la notificacion
        const val JUKEBOX_NOTIFICATION_ID = 1935
        // constante para manejar los canales de comunicacion (para las NOTIFICACIONES)
        const val JUKEBOX_NOTIFICATION_NAME = "nogasJukebox"

    }

    private lateinit var songs: Array<String>           // list of all songs
    private var media : MediaPlayer? = null             // media player for current song
    private var index = -1                              // current song index being played

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        songs = resources.getStringArray(R.array.song_titles)

        // llamando a nuestra FUNCION de NOTIFICACION
        makePlayerNotification()

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


    //  creates a Notification that will "stick around"
    //  with play/stop/.. buttons on it.
    private fun makePlayerNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val channel = NotificationChannel(
                JUKEBOX_NOTIFICATION_ID.toString(),
                JUKEBOX_NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            manager.createNotificationChannel(channel)
            val builder = Notification.Builder(this, JUKEBOX_NOTIFICATION_NAME)

            // ESTE ES EL CODIGO base para crear una notificacion

            builder.setContentTitle("tittle")
                .setContentTitle("text")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.jukebox)

            val stopIntent = Intent(this,JukeboxService::class.java)
            stopIntent.action = STOP_ACTION

            val pending = PendingIntent.getService(
                this,0,stopIntent,0
            )


            // CODIGO PARA MULTIPLES ACCIONES:

            val stopAction = Notification.Action.Builder(
                R.drawable.stop_icon,
                "Stop",
                pending
            ).build()

            builder.addAction(stopAction)

            val notification = builder.build()
            manager.notify(JUKEBOX_NOTIFICATION_ID,notification)



        }
        else{

            // ESTE ES EL CODIGO base para crear una notificacion

            val builder = Notification.Builder(this)
                .setContentTitle("titte")
                .setContentTitle("text")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.jukebox)

            val stopIntent = Intent(this,JukeboxService::class.java)
            stopIntent.action = STOP_ACTION

            val pending = PendingIntent.getService(
                this,0,stopIntent,0
            )


            // CODIGO PARA MULTIPLES ACCIONES:

            val stopAction = Notification.Action.Builder(
                R.drawable.stop_icon,
                "Stop",
                pending
            ).build()

            builder.addAction(stopAction)




            val notification = builder.build()

            val manager = getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            manager.notify(JUKEBOX_NOTIFICATION_ID,notification)



        }

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
