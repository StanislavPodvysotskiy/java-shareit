package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends PagingAndSortingRepository<ItemRequest, Integer> {

    List<ItemRequest> findByRequesterId(Integer requesterId, Sort sort);

}
