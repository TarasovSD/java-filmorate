package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

@RestControllerAdvice(assignableTypes = {FilmController.class, InMemoryFilmStorage.class, InMemoryUserStorage.class,
        UserController.class, FilmService.class, MPAController.class, GenreController.class})
public class FilmExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException() {
        return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException() {
        return new ResponseEntity<>("Object Not Found", HttpStatus.NOT_FOUND);
    }
}
