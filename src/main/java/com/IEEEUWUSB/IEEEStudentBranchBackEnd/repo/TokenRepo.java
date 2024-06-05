package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token, Integer> {
    @Query(value = """
                select t from Token t inner join User u\s
                on t.user.userID = u.userID\s
                where u.userID = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findActiveTokensByUserID(Integer id);

    Optional<Token> findByToken(String token);
}