package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.model.LastBooking;
import ru.practicum.shareit.booking.model.NextBooking;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private ItemResponseDto itemtDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        itemtDto = new ItemResponseDto();
        itemtDto.setId(1);
        itemtDto.setName("name");
        itemtDto.setDescription("description");
        itemtDto.setAvailable(true);
        itemtDto.setRequestId(1);
        itemtDto.setLastBooking(new LastBooking(1, 1));
        itemtDto.setNextBooking(new NextBooking(2,1));
        itemtDto.setComments(Collections.emptyList());
    }

    @Test
    public void save() throws Exception {
        when(itemService.save(any(), anyInt()))
                .thenReturn(itemtDto);
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemtDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemtDto.getId())))
                .andExpect(jsonPath("$.name", is(itemtDto.getName())))
                .andExpect(jsonPath("$.description", is(itemtDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemtDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemtDto.getRequestId())))
                .andExpect(jsonPath("$.lastBooking", is(notNullValue())))
                .andExpect(jsonPath("$.lastBooking.id", is(itemtDto.getLastBooking().getId())))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(itemtDto.getLastBooking().getBookerId())))
                .andExpect(jsonPath("$.nextBooking", is(notNullValue())))
                .andExpect(jsonPath("$.nextBooking.id", is(itemtDto.getNextBooking().getId())))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(itemtDto.getNextBooking().getBookerId())))
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }

    @Test
    public void update() throws Exception {
        itemtDto.setName("updateName");
        itemtDto.setDescription("updateDescription");
        itemtDto.setAvailable(false);
        when(itemService.update(any(), anyInt(), anyInt()))
                .thenReturn(itemtDto);
        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemtDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemtDto.getId())))
                .andExpect(jsonPath("$.name", is(itemtDto.getName())))
                .andExpect(jsonPath("$.description", is(itemtDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemtDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemtDto.getRequestId())))
                .andExpect(jsonPath("$.lastBooking", is(notNullValue())))
                .andExpect(jsonPath("$.lastBooking.id", is(itemtDto.getLastBooking().getId())))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(itemtDto.getLastBooking().getBookerId())))
                .andExpect(jsonPath("$.nextBooking", is(notNullValue())))
                .andExpect(jsonPath("$.nextBooking.id", is(itemtDto.getNextBooking().getId())))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(itemtDto.getNextBooking().getBookerId())))
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }

    @Test
    public void getAll() throws Exception {
        when(itemService.getAll(anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(itemtDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemtDto.getId())))
                .andExpect(jsonPath("$[0].name", is(itemtDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemtDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemtDto.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemtDto.getRequestId())))
                .andExpect(jsonPath("$[0].lastBooking", is(notNullValue())))
                .andExpect(jsonPath("$[0].lastBooking.id", is(itemtDto.getLastBooking().getId())))
                .andExpect(jsonPath("$[0].lastBooking.bookerId", is(itemtDto.getLastBooking().getBookerId())))
                .andExpect(jsonPath("$[0].nextBooking", is(notNullValue())))
                .andExpect(jsonPath("$[0].nextBooking.id", is(itemtDto.getNextBooking().getId())))
                .andExpect(jsonPath("$[0].nextBooking.bookerId", is(itemtDto.getNextBooking().getBookerId())))
                .andExpect(jsonPath("$[0].comments", hasSize(0)));
    }

    @Test
    public void getById() throws Exception {
        when(itemService.getById(anyInt(), anyInt()))
                .thenReturn(itemtDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemtDto.getId())))
                .andExpect(jsonPath("$.name", is(itemtDto.getName())))
                .andExpect(jsonPath("$.description", is(itemtDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemtDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemtDto.getRequestId())))
                .andExpect(jsonPath("$.lastBooking", is(notNullValue())))
                .andExpect(jsonPath("$.lastBooking.id", is(itemtDto.getLastBooking().getId())))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(itemtDto.getLastBooking().getBookerId())))
                .andExpect(jsonPath("$.nextBooking", is(notNullValue())))
                .andExpect(jsonPath("$.nextBooking.id", is(itemtDto.getNextBooking().getId())))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(itemtDto.getNextBooking().getBookerId())))
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }

    @Test
    public void search() throws Exception {
        when(itemService.search(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemtDto));

        mvc.perform(get("/items/search")
                        .param("text", "text")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemtDto.getId())))
                .andExpect(jsonPath("$[0].name", is(itemtDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemtDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemtDto.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemtDto.getRequestId())))
                .andExpect(jsonPath("$[0].lastBooking", is(notNullValue())))
                .andExpect(jsonPath("$[0].lastBooking.id", is(itemtDto.getLastBooking().getId())))
                .andExpect(jsonPath("$[0].lastBooking.bookerId", is(itemtDto.getLastBooking().getBookerId())))
                .andExpect(jsonPath("$[0].nextBooking", is(notNullValue())))
                .andExpect(jsonPath("$[0].nextBooking.id", is(itemtDto.getNextBooking().getId())))
                .andExpect(jsonPath("$[0].nextBooking.bookerId", is(itemtDto.getNextBooking().getBookerId())))
                .andExpect(jsonPath("$[0].comments", hasSize(0)));
    }

    @Test
    public void saveComment() throws Exception {
        CommentResponseDto commentDto = new CommentResponseDto();
        commentDto.setId(1);
        commentDto.setText("text");
        commentDto.setAuthorName("name");
        when(itemService.saveComment(any(), anyInt(), anyInt()))
                .thenReturn(commentDto);
        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }
}
