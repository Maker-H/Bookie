package web.bookie.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import web.bookie.dto.request.BookRequestDTO;
import web.bookie.dto.response.BookResponseDTO;
import web.bookie.service.BookService;
import web.bookie.util.api.ApiResponseBuilder;
import web.bookie.util.api.ApiSuccessResponse;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/book", consumes = "application/json", produces = "application/json")
public class BookController {

    private final BookService bookService;

    private final ApiResponseBuilder apiResponseBuilder;

    @PostMapping(value = "/register")
    public ApiSuccessResponse register(@RequestBody final BookRequestDTO request) {
        BookResponseDTO responseDTO = bookService.register(request);
        return apiResponseBuilder.sendSuccess(responseDTO);
    }
}
