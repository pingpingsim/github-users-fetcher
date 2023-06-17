package com.tawk.githubusers.data.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawk.githubusers.data.entities.User

@Dao
interface UserDao {
    @Query("SELECT COUNT(id) FROM users")
    suspend fun getCount():Int

    @Query("UPDATE users SET notes = :notes WHERE id = :id")
    suspend fun updateUserNotes(notes: String, id: Int)

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUser(id: Int): LiveData<User>

    @Query("SELECT * FROM users")
    fun getAllUsersPagingSource(): PagingSource<Int, User>

    @Query("SELECT * FROM users where notes LIKE '%' || :query || '%' or login LIKE '%' || :query || '%' ")
    fun getFilteredUsersPagingSource(query: String): PagingSource<Int, User>

    @Query("DELETE FROM users")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
}