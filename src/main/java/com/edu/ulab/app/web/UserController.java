package com.edu.ulab.app.web;

import com.edu.ulab.app.facade.UserDataFacade;
import com.edu.ulab.app.web.constant.WebConstant;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.BaseWebResponse;
import com.edu.ulab.app.web.response.UserBookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

import static com.edu.ulab.app.web.constant.WebConstant.REQUEST_ID_PATTERN;
import static com.edu.ulab.app.web.constant.WebConstant.RQID;

@Slf4j
@RestController
@RequestMapping(
        value = WebConstant.VERSION_URL + "/user",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class UserController {

    private final UserDataFacade userDataFacade;

    public UserController(UserDataFacade userDataFacade) {
        this.userDataFacade = userDataFacade;
    }

    @PostMapping(value = "/create")
    @Operation(
            summary = "Create user with his books.",
            responses = {
                    @ApiResponse(description = "User and his books",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserBookResponse.class)
                            )
                    )
            }
    )
    public UserBookResponse createUserWithBooks(@RequestBody UserBookRequest request,

                                                @RequestHeader(RQID)
                                                @Pattern(regexp = REQUEST_ID_PATTERN) final String requestId) {
        UserBookResponse response = userDataFacade.createUserWithBooks(request);
        log.info("Response with created user and his books: {}", response);
        return response;
    }

    @PutMapping(value = "/update/{userId}")
    @Operation(
            summary = "Update user and add books.",
            responses = {
                    @ApiResponse(description = "User ID and books ID",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserBookResponse.class)
                            )
                    )
            }
    )
    public UserBookResponse updateUserWithBooks(@RequestBody UserBookRequest request,
                                                @PathVariable Long userId,

                                                @RequestHeader(RQID)
                                                @Pattern(regexp = REQUEST_ID_PATTERN) final String requestId) {
        UserBookResponse response = userDataFacade.updateUserWithBooks(request, userId);
        log.info("Response with updated user and his books: {}", response);
        return response;
    }

    @GetMapping(value = "/get/{userId}")
    @Operation(
            summary = "Get user and his books.",
            responses = {
                    @ApiResponse(description = "User ID and books ID",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserBookResponse.class)
                            )
                    )
            }
    )
    public UserBookResponse getUserWithBooks(@PathVariable Long userId,

                                             @RequestHeader(RQID)
                                             @Pattern(regexp = REQUEST_ID_PATTERN) final String requestId) {
        UserBookResponse response = userDataFacade.getUserWithBooks(userId);
        log.info("Response with user and his books: {}", response);
        return response;
    }

    @DeleteMapping(value = "/delete/{userId}")
    @Operation(
            summary = "Delete user and their books.",
            responses = {
                    @ApiResponse(description = "Result message",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseWebResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<BaseWebResponse> deleteUserWithBooks(@PathVariable Long userId) {
        log.info("Delete user and his books:  userId {}", userId);
        return userDataFacade.deleteUserWithBooks(userId);
    }
}
