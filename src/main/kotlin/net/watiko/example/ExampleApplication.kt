package net.watiko.example

import net.watiko.example.Controller.BookController
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

fun main(args: Array<String>) {
    runApplication<ExampleApplication>(*args)
}

@SpringBootApplication
class ExampleApplication {
    @Bean
    fun routes(bookController: BookController): RouterFunction<ServerResponse> {
        return router {
            "/api".nest {
                "/books".nest {
                    accept(MediaType.APPLICATION_JSON_UTF8).nest {
                        GET("", bookController::listBooks)
                        POST("", bookController::addBook)
                    }
                }
            }
        }
    }
}
