package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BookingResponseDto {

    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;
    private User booker;
    private Item item;

}
