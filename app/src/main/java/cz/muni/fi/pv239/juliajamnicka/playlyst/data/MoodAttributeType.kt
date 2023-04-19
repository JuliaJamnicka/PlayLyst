package cz.muni.fi.pv239.juliajamnicka.playlyst.data
data class AttributeThresholds(
    val minValue: Double,
    val maxValue: Double,
    val stepSize: Double,
    val canHaveRange: Boolean,
    val defaultValue: Double? = null,
    val lowerDefaultValue: Double? = null,
    val upperDefaultValue: Double? = null,
)

enum class MoodAttributeType {
    DANCEABILITY, ACOUSTICNESS, POPULARITY, INSTRUMENTALNESS, ENERGY, LIVENESS,
    LOUDNESS, SPEECHINESS, TEMPO, VALENCE; //KEY, MODE

    fun getDescription(): String = when (this) {
        //KEY -> "The estimated overall key of the track. Integers map to pitches using standard Pitch Class notation. Ex: 0 = C, 1 = C♯/D♭, 2 = D, and so on. If no key was detected, the value is -1."
        //MODE -> "Mode indicates the modality (major or minor) of a track, the type of scale from which its melodic content is derived. Major is represented by 1 and minor is 0."
        ACOUSTICNESS -> "A confidence measure from 0.0 to 1.0 of whether the track is acoustic. 1.0 represents high confidence the track is acoustic."
        DANCEABILITY -> "Danceability describes how suitable a track is for dancing based on a combination of musical elements including tempo, rhythm stability, beat strength, and overall regularity. A value of 0.0 is least danceable and 1.0 is the most danceable."
        ENERGY -> "Energy is a measure from 0.0 to 1.0 and represents a perceptual measure of intensity and activity. Typically, energetic tracks feel fast, loud, and noisy."
        INSTRUMENTALNESS -> "Predicts whether a track contains no vocals. The closer the instrumentalness value is to 1.0, the greater likelihood the track contains no vocal content."
        LOUDNESS -> "The overall loudness of a track in decibels (dB). Values typical range between -60 and 0 dB."
        VALENCE -> "A measure from 0.0 to 1.0 describing the musical positiveness conveyed by a track. Tracks with high valence sound more positive."
        TEMPO -> "The overall estimated tempo of a track in beats per minute (BPM)."
        POPULARITY -> "The popularity of the track. The value will be between 0 and 100, with 100 being the most popular."
        LIVENESS -> "Detects the presence of an audience in the recording. Higher liveness values represent an increased probability that the track was performed live. A value above 0.8 provides strong likelihood that the track is live."
        SPEECHINESS -> "Speechiness detects the presence of spoken words in a track. The more exclusively speech-like the recording (e.g. talk show, audio book, poetry), the closer to 1.0 the attribute value. Values above 0.66 describe tracks that are probably made entirely of spoken words. Values between 0.33 and 0.66 describe tracks that may contain both music and speech, either in sections or layered, including such cases as rap music. Values below 0.33 most likely represent music and other non-speech-like tracks."
    }

    fun getThresholds(): AttributeThresholds = when (this) {
        /*KEY -> AttributeThresholds(
            minValue = -1.0,
            maxValue = 11.0,
            stepSize = 1.0,
            canHaveRange = false,
            defaultValue = 5.0
        )
        MODE -> AttributeThresholds(
            minValue = 0.0,
            maxValue = 1.0,
            stepSize = 1.0,
            canHaveRange = false,
            defaultValue = 1.0
        )*/
        ACOUSTICNESS -> AttributeThresholds(
            minValue = 0.0,
            maxValue = 1.0,
            stepSize = 0.05,
            canHaveRange = true,
            lowerDefaultValue = 0.5,
            upperDefaultValue = 1.0
        )
        DANCEABILITY -> AttributeThresholds(
            minValue = 0.0,
            maxValue = 1.0,
            stepSize = 0.05,
            canHaveRange = true,
            lowerDefaultValue = 0.35,
            upperDefaultValue = 0.65
        )
        ENERGY -> AttributeThresholds(
            minValue = 0.0,
            maxValue = 1.0,
            stepSize = 0.05,
            canHaveRange = true,
            lowerDefaultValue = 0.35,
            upperDefaultValue = 0.65
        )
        INSTRUMENTALNESS -> AttributeThresholds(
            minValue = 0.0,
            maxValue = 1.0,
            stepSize = 0.05,
            canHaveRange = true,
            lowerDefaultValue = 0.5,
            upperDefaultValue = 1.0
        )
        LOUDNESS -> AttributeThresholds(
            minValue = -60.0,
            maxValue = 0.0,
            stepSize = 5.0,
            canHaveRange = true,
            lowerDefaultValue = -60.0,
            upperDefaultValue = 0.0
        )
        VALENCE -> AttributeThresholds(
            minValue = 0.0,
            maxValue = 1.0,
            stepSize = 0.05,
            canHaveRange = true,
            lowerDefaultValue = 0.0,
            upperDefaultValue = 1.0
        )
        TEMPO -> AttributeThresholds(
            minValue = 0.0,
            maxValue = 300.0,
            stepSize = 10.0,
            canHaveRange = true,
            lowerDefaultValue = 10.0,
            upperDefaultValue = 200.0
        )
        POPULARITY -> AttributeThresholds(
            minValue = 0.0,
            maxValue = 100.0,
            stepSize = 5.0,
            canHaveRange = true,
            lowerDefaultValue = 20.0,
            upperDefaultValue = 100.0
        )
        LIVENESS -> AttributeThresholds(
            minValue = 0.0,
            maxValue = 1.0,
            stepSize = 0.05,
            canHaveRange = true,
            lowerDefaultValue = 0.75,
            upperDefaultValue = 1.0
        )
        SPEECHINESS -> AttributeThresholds(
            minValue = 0.0,
            maxValue = 1.0,
            stepSize = 0.05,
            canHaveRange = true,
            lowerDefaultValue = 0.35,
            upperDefaultValue = 0.65
        )
    }
}