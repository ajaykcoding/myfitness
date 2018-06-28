package com.myfitness.chatserver.repository;

import com.myfitness.chatserver.entity.Message;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextRepository extends PagingAndSortingRepository<Message, Long> {

    List<Message> findByUserName(String userName);

    List<Message> findByUserNameAndEpochTimeGreaterThan(String userName, long epochTime);

    List<Message> findByEpochTimeLessThan(long epochTime);


}
