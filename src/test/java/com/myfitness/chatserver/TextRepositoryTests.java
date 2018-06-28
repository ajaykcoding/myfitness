package com.myfitness.chatserver;

import com.myfitness.chatserver.entity.Message;
import com.myfitness.chatserver.repository.TextRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TextRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TextRepository textRepository;


    @Test
    public void whenFindByUserName_thenReturnTexts() {
        // given
        Message m1 = new Message();
        m1.setText("text");
        m1.setUserName("user1");
        entityManager.persist(m1);
        entityManager.flush();

        // when
        List<Message> found = textRepository.findByUserName(m1.getUserName());
        //then
        Assert.assertEquals("user1", found.get(0).getUserName());
    }

    @Test
    public void whenFindByUserNameNotFound_thenReturnEmptyList() {
        // given


        // when
        List<Message> found = textRepository.findByUserName("nonexistinguser");
        Assert.assertEquals(0, found.size());

    }


    @Test
    public void whenFindByUserNameAndCurrentTime_thenReturnValidMessage() {
        // given
        Message m1 = new Message();
        m1.setText("text");
        m1.setUserName("user1");
        m1.setEpochTime(System.currentTimeMillis() + 100 * 1000);
        entityManager.persist(m1);
        entityManager.flush();


        // when
        List<Message> found = textRepository.findByUserNameAndEpochTimeGreaterThan("user1", System.currentTimeMillis());
        Assert.assertEquals(1, found.size());

    }


    @Test
    public void whenFindByTImeLessThanCurrentTime_thenReturnEmptyList() {
        // given
        Message m1 = new Message();
        m1.setText("text");
        m1.setUserName("user1");
        m1.setEpochTime(System.currentTimeMillis() - 100 * 1000);
        entityManager.persist(m1);
        entityManager.flush();


        // when
        List<Message> found = textRepository.findByEpochTimeLessThan(System.currentTimeMillis());
        Assert.assertEquals(1, found.size());

    }


}
