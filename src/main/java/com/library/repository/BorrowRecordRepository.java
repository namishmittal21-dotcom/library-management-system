package com.library.repository;

import com.library.model.BorrowRecord;
import com.library.model.Book;
import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUserAndReturnedFalse(User user);
    List<BorrowRecord> findByBookAndReturnedFalse(Book book);  // Add this line
    List<BorrowRecord> findByBook(Book book);  // Add this line (optional)
    long countByReturnedFalse();
    @Query("SELECT COUNT(b) FROM BorrowRecord b WHERE b.returned = false AND b.dueDate < :today")
    long countOverdueBooks(@Param("today") LocalDate today);
    List<BorrowRecord> findByReturnedFalseAndDueDateBefore(LocalDate date);
}