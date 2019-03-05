package net.watiko.example.Controller

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.LinkedHashMap
import java.util.Collections

data class Book(val id: Int, val title: String)
data class CreateBookReq(val title: String)

class BookRepository {
    private val values = mutableListOf<Book>()
    private var cursor = 0

    fun create(req: CreateBookReq): Mono<Book> {
        val book = Book(cursor++, req.title)
        values.add(book)
        return Mono.just(book)
    }

    fun findAll(): Flux<Book> = Flux.fromIterable(values)
}

@Component
class BookController {
    val bookRepository = BookRepository()

    fun addBook(req: ServerRequest) = req.bodyToMono(CreateBookReq::class.java)
            .flatMap(bookRepository::create)
            .flatMap { book ->
                ServerResponse.ok().syncBody("added: ${book.id}, ${book.title}")
            }

    fun listBooks(req: ServerRequest) = ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(bookRepository.findAll())
}
