package com.myfitness.chatserver.repository;

import com.myfitness.chatserver.entity.ExpiredMessage;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextBackupRepository extends PagingAndSortingRepository<ExpiredMessage, Long> {
}
