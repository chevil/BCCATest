package com.github.bccatest.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import com.google.gson.annotations.SerializedName

@Entity(tableName = "albums")
data class AlbumEntity (

    // primary auto-increment id
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Long?,

    // external album id
    @SerializedName("albumId")
    val albumId: Long,

    // album title
    @SerializedName("title")
    val title: String,

    // url of the cover art
    @SerializedName("url")
    val cover: String,

    // url of the small cover art
    @SerializedName("thumbnailUrl")
    val thumbnail: String
)
