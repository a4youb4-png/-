package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Favorites
    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE itemType = :type ORDER BY timestamp DESC")
    fun getFavoritesByType(type: String): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    fun isFavorite(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavoriteById(id: String)

    // Daily Tracker
    @Query("SELECT * FROM daily_tracker WHERE dateKey = :dateKey")
    fun getDailyTracker(dateKey: String): Flow<DailyTrackerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyTracker(tracker: DailyTrackerEntity)

    // Tasbih Counters
    @Query("SELECT * FROM tasbih_counters")
    fun getAllTasbih(): Flow<List<TasbihEntity>>

    @Query("SELECT * FROM tasbih_counters WHERE dhikrId = :dhikrId")
    suspend fun getTasbih(dhikrId: String): TasbihEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasbih(tasbih: TasbihEntity)

    // Quran Bookmark
    @Query("SELECT * FROM quran_bookmarks WHERE id = 1")
    fun getBookmark(): Flow<QuranBookmarkEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBookmark(bookmark: QuranBookmarkEntity)
}
