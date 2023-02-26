package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.utility.Create;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BookingDto {

    @NotNull(groups = {Create.class})
    @Future(groups = {Create.class})
    private LocalDateTime start;
    @NotNull(groups = {Create.class})
    @Future(groups = {Create.class})
    private LocalDateTime end;
    private String status;
    private Integer itemId;

}
