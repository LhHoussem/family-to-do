package com.mycompany.familytodo.service.impl;

import com.mycompany.familytodo.domain.ToDoList;
import com.mycompany.familytodo.repository.ToDoListRepository;
import com.mycompany.familytodo.service.ToDoListService;
import com.mycompany.familytodo.service.dto.ToDoListDTO;
import com.mycompany.familytodo.service.mapper.ToDoListMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link ToDoList}.
 */
@Service
public class ToDoListServiceImpl implements ToDoListService {

    private final Logger log = LoggerFactory.getLogger(ToDoListServiceImpl.class);

    private final ToDoListRepository toDoListRepository;

    private final ToDoListMapper toDoListMapper;

    public ToDoListServiceImpl(ToDoListRepository toDoListRepository, ToDoListMapper toDoListMapper) {
        this.toDoListRepository = toDoListRepository;
        this.toDoListMapper = toDoListMapper;
    }

    @Override
    public ToDoListDTO save(ToDoListDTO toDoListDTO) {
        log.debug("Request to save ToDoList : {}", toDoListDTO);
        ToDoList toDoList = toDoListMapper.toEntity(toDoListDTO);
        toDoList = toDoListRepository.save(toDoList);
        return toDoListMapper.toDto(toDoList);
    }

    @Override
    public ToDoListDTO update(ToDoListDTO toDoListDTO) {
        log.debug("Request to update ToDoList : {}", toDoListDTO);
        ToDoList toDoList = toDoListMapper.toEntity(toDoListDTO);
        //        toDoList.setIsPersisted();
        toDoList = toDoListRepository.save(toDoList);
        return toDoListMapper.toDto(toDoList);
    }

    @Override
    public Optional<ToDoListDTO> partialUpdate(ToDoListDTO toDoListDTO) {
        log.debug("Request to partially update ToDoList : {}", toDoListDTO);

        return toDoListRepository
            .findById(toDoListDTO.getId())
            .map(existingToDoList -> {
                toDoListMapper.partialUpdate(existingToDoList, toDoListDTO);

                return existingToDoList;
            })
            .map(toDoListRepository::save)
            .map(toDoListMapper::toDto);
    }

    @Override
    public Page<ToDoListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ToDoLists");
        return toDoListRepository.findAll(pageable).map(toDoListMapper::toDto);
    }

    public Page<ToDoListDTO> findAllWithEagerRelationships(Pageable pageable) {
        return toDoListRepository.findAllWithEagerRelationships(pageable).map(toDoListMapper::toDto);
    }

    @Override
    public Optional<ToDoListDTO> findOne(String id) {
        log.debug("Request to get ToDoList : {}", id);
        return toDoListRepository.findOneWithEagerRelationships(id).map(toDoListMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete ToDoList : {}", id);
        toDoListRepository.deleteById(id);
    }
}
