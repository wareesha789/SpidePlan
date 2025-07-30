package com.wish.spideplan.data.repository

import com.wish.spideplan.data.database.dao.QuoteDao
import com.wish.spideplan.data.model.Quote
import com.wish.spideplan.data.model.QuoteCategory
import kotlinx.coroutines.flow.Flow
class QuoteRepository(
    private val quoteDao: QuoteDao
) {
    
    suspend fun getRandomQuote(): Quote? = quoteDao.getRandomQuote()
    
    suspend fun getRandomQuoteByCategory(category: QuoteCategory): Quote? = 
        quoteDao.getRandomQuoteByCategory(category)
    
    fun getAllQuotes(): Flow<List<Quote>> = quoteDao.getAllQuotes()
    
    fun getFavoriteQuotes(): Flow<List<Quote>> = quoteDao.getFavoriteQuotes()
    
    fun getQuotesByCategory(category: QuoteCategory): Flow<List<Quote>> = 
        quoteDao.getQuotesByCategory(category)
    
    suspend fun getQuoteById(id: Long): Quote? = quoteDao.getQuoteById(id)
    
    suspend fun insertQuote(quote: Quote): Long = quoteDao.insertQuote(quote)
    
    suspend fun insertQuotes(quotes: List<Quote>) = quoteDao.insertQuotes(quotes)
    
    suspend fun updateQuote(quote: Quote) = quoteDao.updateQuote(quote)
    
    suspend fun deleteQuote(quote: Quote) = quoteDao.deleteQuote(quote)
    
    suspend fun updateQuoteFavoriteStatus(id: Long, isFavorite: Boolean) = 
        quoteDao.updateQuoteFavoriteStatus(id, isFavorite)
    
    suspend fun getQuoteCount(): Int = quoteDao.getQuoteCount()
    
    suspend fun deleteAllQuotes() = quoteDao.deleteAllQuotes()
    
    suspend fun initializeDefaultQuotes() {
        if (getQuoteCount() == 0) {
            val defaultQuotes = getDefaultSpiderManQuotes()
            insertQuotes(defaultQuotes)
        }
    }
    
    private fun getDefaultSpiderManQuotes(): List<Quote> {
        return listOf(
            Quote(
                text = "With great power comes great responsibility.",
                author = "Uncle Ben",
                source = "Spider-Man (2002)",
                category = QuoteCategory.RESPONSIBILITY
            ),
            Quote(
                text = "Sometimes we have to be steady and give up the thing we want the most. Even our dreams.",
                author = "Spider-Man",
                source = "Spider-Man (2002)",
                category = QuoteCategory.PERSEVERANCE
            ),
            Quote(
                text = "Not everyone is meant to make a difference. But for me, the choice to lead an ordinary life is no longer an option.",
                author = "Spider-Man",
                source = "The Amazing Spider-Man",
                category = QuoteCategory.COURAGE
            ),
            Quote(
                text = "We all have secrets: the ones we keep... and the ones that are kept from us.",
                author = "Spider-Man",
                source = "The Amazing Spider-Man",
                category = QuoteCategory.WISDOM
            ),
            Quote(
                text = "The only way to live a good life is to act on your emotions.",
                author = "Spider-Man",
                source = "The Amazing Spider-Man 2",
                category = QuoteCategory.MOTIVATION
            ),
            Quote(
                text = "I believe there's a hero in all of us, that keeps us honest, gives us strength, makes us noble.",
                author = "Aunt May",
                source = "Spider-Man 2",
                category = QuoteCategory.MOTIVATION
            ),
            Quote(
                text = "No matter how buried it gets, or how lost you feel, you must promise me that you will hold on to hope.",
                author = "Aunt May",
                source = "The Amazing Spider-Man 2",
                category = QuoteCategory.PERSEVERANCE
            ),
            Quote(
                text = "It's the choices that make us who we are, and we can always choose to do what's right.",
                author = "Spider-Man",
                source = "Spider-Man 3",
                category = QuoteCategory.RESPONSIBILITY
            ),
            Quote(
                text = "Whatever comes our way, whatever battle we have raging inside us, we always have a choice.",
                author = "Spider-Man",
                source = "Spider-Man 3",
                category = QuoteCategory.COURAGE
            ),
            Quote(
                text = "Being Spider-Man is not about the mask, it's about having the courage to do what's right.",
                author = "Spider-Man",
                source = "Spider-Man: Homecoming",
                category = QuoteCategory.COURAGE
            ),
            Quote(
                text = "You don't become a hero because you have powers. You become a hero by using them to help others.",
                author = "Spider-Man",
                source = "Spider-Man",
                category = QuoteCategory.RESPONSIBILITY
            ),
            Quote(
                text = "The hardest thing about being Spider-Man is that you can't always save everyone.",
                author = "Spider-Man",
                source = "Spider-Man",
                category = QuoteCategory.WISDOM
            )
        )
    }
}