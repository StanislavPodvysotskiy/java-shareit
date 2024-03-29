package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("select i from Item i where i.owner.id = ?1")
    Page<Item> findAllByOwnerId(Integer ownerID, Pageable pageable);

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')) and i.available = true")
    Page<Item> search(String text, Pageable pageable);

    @Query("select i from Item i where i.itemRequest.id = ?1")
    List<Item> findByRequestId(Integer requestId);

    @Query("select i from Item i where i.itemRequest.requesterId = ?1")
    List<Item> findAllByRequesterId(Integer requesterId);

    @Query("select i from Item i where i.itemRequest.requesterId <> ?1 and i.itemRequest.requesterId is not null")
    List<Item> findAllNotEqualRequesterId(Integer requesterId);
}
