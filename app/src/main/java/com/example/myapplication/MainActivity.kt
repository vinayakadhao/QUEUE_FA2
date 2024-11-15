package com.example.myapplication


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var borrowedBooksList: TextView
    private lateinit var reservedBooksList: TextView
    private lateinit var statusMessage: TextView
    private lateinit var bookTitleInput: EditText
    private lateinit var borrowLimitInput: EditText
    private lateinit var reserveLimitInput: EditText
    private lateinit var setLimitsButton: Button
    private val borrowedBooks: MutableList<String> = mutableListOf()
    private val reservedBooks: MutableList<String> = mutableListOf()
    private var MAX_BORROWED_BOOKS = 5
    private var MAX_RESERVED_BOOKS = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        borrowedBooksList = findViewById(R.id.borrowed_books_list)
        reservedBooksList = findViewById(R.id.reserved_books_list)
        statusMessage = findViewById(R.id.status_message)
        bookTitleInput = findViewById(R.id.book_title_input)
        borrowLimitInput = findViewById(R.id.borrow_limit_input)
        reserveLimitInput = findViewById(R.id.reserve_limit_input)
        setLimitsButton = findViewById(R.id.set_limits_button)

        val borrowButton: Button = findViewById(R.id.borrow_button)
        val reserveButton: Button = findViewById(R.id.reserve_button)
        val returnButton: Button = findViewById(R.id.return_button)

        // Set limits button action
        setLimitsButton.setOnClickListener {
            setLimits()
        }

        borrowButton.setOnClickListener {
            borrowBook()
        }

        reserveButton.setOnClickListener {
            reserveBook()
        }

        returnButton.setOnClickListener {
            returnBook()
        }
    }

    private fun setLimits() {
        val borrowLimit = borrowLimitInput.text.toString().trim()
        val reserveLimit = reserveLimitInput.text.toString().trim()

        if (borrowLimit.isNotEmpty()) {
            MAX_BORROWED_BOOKS = borrowLimit.toInt()
            borrowLimitInput.text.clear() // Clear input after setting limit
        }

        if (reserveLimit.isNotEmpty()) {
            MAX_RESERVED_BOOKS = reserveLimit.toInt()
            reserveLimitInput.text.clear() // Clear input after setting limit
        }
        statusMessage.text = "Limits updated successfully!"
    }

    private fun borrowBook() {
        val bookTitle = bookTitleInput.text.toString().trim()
        if (bookTitle.isNotEmpty() && borrowedBooks.size < MAX_BORROWED_BOOKS) { // Limit to user-defined max
            borrowedBooks.add(bookTitle)
            updateBorrowedBooksDisplay()
            bookTitleInput.text.clear() // Clear input after borrowing
            statusMessage.text = ""
        } else if (borrowedBooks.size >= MAX_BORROWED_BOOKS) {
            statusMessage.text = "You can't borrow more than $MAX_BORROWED_BOOKS books."
        } else {
            statusMessage.text = "Please enter a book title."
        }
    }

    private fun reserveBook() {
        val bookTitle = bookTitleInput.text.toString().trim()
        if (bookTitle.isNotEmpty() && reservedBooks.size < MAX_RESERVED_BOOKS) { // Limit to user-defined max
            reservedBooks.add(bookTitle)
            updateReservedBooksDisplay()
            bookTitleInput.text.clear() // Clear input after reserving
            statusMessage.text = ""
        } else if (reservedBooks.size >= MAX_RESERVED_BOOKS) {
            statusMessage.text = "You can't reserve more than $MAX_RESERVED_BOOKS books."
        } else {
            statusMessage.text = "Please enter a book title."
        }
    }

    private fun returnBook() {
        if (borrowedBooks.isNotEmpty()) {
            borrowedBooks.removeAt(0) // Return the first book (FIFO)
            updateBorrowedBooksDisplay()

            // Check if there's a reserved book to move to borrowed
            if (reservedBooks.isNotEmpty() && borrowedBooks.size < MAX_BORROWED_BOOKS) {
                val reservedBook = reservedBooks.removeAt(0)
                borrowedBooks.add(reservedBook)
                updateBorrowedBooksDisplay()
                updateReservedBooksDisplay()
                statusMessage.text = "$reservedBook moved to borrowed books."
            } else {
                statusMessage.text = ""
            }
        } else {
            statusMessage.text = "No books to return."
        }
    }

    private fun updateBorrowedBooksDisplay() {
        borrowedBooksList.text = if (borrowedBooks.isEmpty()) {
            "No books borrowed"
        } else {
            borrowedBooks.joinToString(", ")
        }
    }

    private fun updateReservedBooksDisplay() {
        reservedBooksList.text = if (reservedBooks.isEmpty()) {
            "No books reserved"
        } else {
            reservedBooks.joinToString(", ")
        }
    }
}

