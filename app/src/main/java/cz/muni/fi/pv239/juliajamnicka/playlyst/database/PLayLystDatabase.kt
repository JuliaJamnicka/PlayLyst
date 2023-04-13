package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MoodEntity::class,
        PlaylistEntity::class,
        SongEntity::class,
        PLaylistAndSongEntity::class],
version = 1
)
abstract class PLayLystDatabase : RoomDatabase() {
    companion object {
        private const val NAME = "playlyst.db"

        fun create(context: Context): PLayLystDatabase =
            Room.databaseBuilder(context, PLayLystDatabase::class.java, NAME)
                .allowMainThreadQueries()
                .build()
    }

    abstract fun playlistDao(): PlaylistDao
    abstract fun moodDao() : MoodDao
}