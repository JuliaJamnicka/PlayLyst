package cz.muni.fi.pv239.juliajamnicka.playlyst.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [MoodEntity::class,
        MoodAttributeEntity::class,
        PlaylistEntity::class,
        SongEntity::class,
        PlaylistAndSongEntity::class,
        ArtistEntity::class,
        SongAndArtistEntity::class],
version = 1
)
abstract class PlayLystDatabase : RoomDatabase() {
    companion object {
        private const val NAME = "playlyst.db"

        fun create(context: Context): PlayLystDatabase =
            Room.databaseBuilder(context, PlayLystDatabase::class.java, NAME)
                .allowMainThreadQueries()
                .addCallback(object: RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        db.execSQL("INSERT INTO MoodEntity(moodId, name, color) VALUES(1,'Dancing with the sun ☀','#EA9300')")

                        val sunnyAttrs: Map<String, Any> = mapOf(
                            Pair("DANCEABILITY", 1.0),
                            Pair("ENERGY", 0.9),
                            Pair("MODE", 1),
                            Pair("POPULARITY", 90),
                            Pair("VALENCE", 1.0)
                        )
                        for ((name, value) in sunnyAttrs) {
                            db.execSQL("INSERT INTO MoodAttributeEntity(moodEntityId,name,chosenValue) VALUES(1,'$name',$value)")
                        }

                        db.execSQL("INSERT INTO MoodEntity(moodId, name, color) VALUES(2,'Hello darkness, my old friend','#004b90')")

                        val sadAttrs: Map<String, Any> = mapOf(
                            Pair("ACOUSTICNESS", 0.6),
                            Pair("DANCEABILITY", 0.1),
                            Pair("ENERGY", 0.2),
                            Pair("MODE", 0),
                            Pair("POPULARITY", 80),
                            Pair("VALENCE", 0.0)
                        )
                        for ((name, value) in sadAttrs) {
                            db.execSQL("INSERT INTO MoodAttributeEntity(moodEntityId,name,chosenValue) VALUES(2,'$name',$value)")
                        }
                    }
                })
                .build()
    }

    abstract fun playlistDao(): PlaylistDao
    abstract fun moodDao() : MoodDao
}