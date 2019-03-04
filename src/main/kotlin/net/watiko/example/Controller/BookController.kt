package net.watiko.example.Controller

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import java.util.LinkedHashMap
import java.util.Collections

data class Book(val id: String, val title: String)

@Component
class BookController {
    val bookRepository: MutableMap<String, Book> = Collections.synchronizedMap(LinkedHashMap<String, Book>())

    private fun getAllBooks() = bookRepository.values.toList()

    fun addBook(req: ServerRequest) = req.bodyToMono(Book::class.java)
            .flatMap { book ->
                bookRepository[book.id] = book
                ServerResponse.ok().syncBody("added: ${book.id}, ${book.title}")
            }

    fun listBooks(req: ServerRequest) = ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .syncBody(getAllBooks())
}
