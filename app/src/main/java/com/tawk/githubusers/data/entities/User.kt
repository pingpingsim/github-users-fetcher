package com.tawk.githubusers.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("id")
    val id: Int,
    @ColumnInfo(name = "login")
    @field:SerializedName("login")
    val login: String,
    @ColumnInfo(name = "node_id")
    @field:SerializedName("node_id")
    val nodeId: String,
    @ColumnInfo(name = "avatar_url")
    @field:SerializedName("avatar_url")
    val avatarUrl: String,
    @ColumnInfo(name = "gravatar_id")
    @field:SerializedName("gravatar_id")
    val gravatarId: String,
    @ColumnInfo(name = "url")
    @field:SerializedName("url")
    val url: String,
    @ColumnInfo(name = "html_url")
    @field:SerializedName("html_url")
    val htmlUrl: String,
    @ColumnInfo(name = "followers_url")
    @field:SerializedName("followers_url")
    val followersUrl: String,
    @ColumnInfo(name = "following_url")
    @field:SerializedName("following_url")
    val followingUrl: String,
    @ColumnInfo(name = "gists_url")
    @field:SerializedName("gists_url")
    val gistsUrl: String,
    @ColumnInfo(name = "starred_url")
    @field:SerializedName("starred_url")
    val starredUrl: String,
    @ColumnInfo(name = "subscriptions_url")
    @field:SerializedName("subscriptions_url")
    val subscriptionsUrl: String,
    @ColumnInfo(name = "organizations_url")
    @field:SerializedName("organizations_url")
    val organizationsUrl: String,
    @ColumnInfo(name = "repos_url")
    @field:SerializedName("repos_url")
    val reposUrl: String,
    @ColumnInfo(name = "events_url")
    @field:SerializedName("events_url")
    val eventsUrl: String,
    @ColumnInfo(name = "received_events_url")
    @field:SerializedName("received_events_url")
    val receivedEventsUrl: String,
    @ColumnInfo(name = "type")
    @field:SerializedName("type")
    val type: String,
    @ColumnInfo(name = "site_admin")
    @field:SerializedName("site_admin")
    val isSiteAdmin: Boolean,
    @ColumnInfo(name = "notes")
    val notes: String?,

    @ColumnInfo(name = "followers")
    @field:SerializedName("followers")
    val followers: Long,
    @ColumnInfo(name = "following")
    @field:SerializedName("following")
    val following: Long,
    @ColumnInfo(name = "name")
    @field:SerializedName("name")
    val name: String,
    @ColumnInfo(name = "company")
    @field:SerializedName("company")
    val company: String,
    @ColumnInfo(name = "blog")
    @field:SerializedName("blog")
    val blog: String,
    @ColumnInfo(name = "location")
    @field:SerializedName("location")
    val location: String,
    @ColumnInfo(name = "email")
    @field:SerializedName("email")
    val email: String,
    @ColumnInfo(name = "bio")
    @field:SerializedName("bio")
    val bio: String,
) : Parcelable
