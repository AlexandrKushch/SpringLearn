package com.khpi.vragu.repos;

import com.khpi.vragu.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Integer> {
    public List<Message> findByTag(String tag);
}
