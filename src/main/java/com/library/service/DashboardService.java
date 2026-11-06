package com.library.service;

import com.library.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class DashboardService {
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private final BorrowRecordRepository borrowRepo;
    
    public DashboardService(BookRepository bookRepo, UserRepository userRepo, BorrowRecordRepository borrowRepo) {
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
        this.borrowRepo = borrowRepo;
    }
    
    public long getTotalBooks() { return bookRepo.count(); }
    public long getTotalMembers() { return userRepo.count() - 1; }
    public long getBooksIssued() { return borrowRepo.countByReturnedFalse(); }
    public long getOverdueBooks() { return borrowRepo.countOverdueBooks(LocalDate.now()); }
}