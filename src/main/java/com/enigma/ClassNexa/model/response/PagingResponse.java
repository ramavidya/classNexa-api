package com.enigma.ClassNexa.model.response;

<<<<<<< HEAD
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PagingResponse {

    @NotNull
    private Long totalElements;

    @NotNull
    private int totalPages;

    @NotNull
    private int page;

    @NotNull
    private int size;

=======
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {
    private Integer page;
    private Integer size;
    private Integer totalPages;
    private Long totalElements;
>>>>>>> dev/putra
}
