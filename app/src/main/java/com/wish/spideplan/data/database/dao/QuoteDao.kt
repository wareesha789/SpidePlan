package com.wish.spideplan.data.database.dao

import androidx.room.*
import com.wish.spideplan.data.model.Quote
import com.wish.spideplan.data.model.QuoteCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    
    @Query("SELECT * FROM quotes ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomQuote(): Quote?
    
    @Query("SELECT * FROM quotes WHERE category = :category ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomQuoteByCategory(category: QuoteCategory): Quote?
    
    @Query("SELECT * FROM quotes ORDER BY id ASC")
    fun getAllQuotes(): Flow<List<Quote>>
    
    @Query("SELECT * FROM quotes WHERE isFavorite = 1 ORDER BY id ASC")
    fun getFavoriteQuotes(): Flow<List<Quote>>
    
    @Query("SELECT * FROM quotes WHERE category = :category ORDER BY id ASC")
    fun getQuotesByCategory(category: QuoteCategory): Flow<List<Quote>>
    
    @Query("SELECT * FROM quotes WHERE id = :id")
    suspend fun getQuoteById(id: Long): Quote?
    
    @Insert
    suspend fun insertQuote(quote: Quote): Long
    
    @Insert
    suspend fun insertQuotes(quotes: List<Quote>)
    
    @Update
    suspend fun updateQuote(quote: Quote)
    
    @Delete
    suspend fun deleteQuote(quote: Quote)
    
    @Query("UPDATE quotes SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateQuoteFavoriteStatus(id: Long, isFavorite: Boolean)
    
    @Query("SELECT COUNT(*) FROM quotes")
    suspend fun getQuoteCount(): Int
    
    @Query("DELETE FROM quotes")
    suspend fun deleteAllQuotes()
}