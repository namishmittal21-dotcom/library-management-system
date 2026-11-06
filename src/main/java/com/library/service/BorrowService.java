package com.library.service;

import com.library.model.*;
import com.library.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowService {
    private final BorrowRecordRepository borrowRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private final EmailService emailService;
    
    public BorrowService(BorrowRecordRepository borrowRepo, BookRepository bookRepo,
                         UserRepository userRepo, EmailService emailService) {
        this.borrowRepo = borrowRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
        this.emailService = emailService;
    }
    
    @Transactional
    public String issueBook(String barcode, String username) {
        Book book = bookRepo.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Book not found with barcode: " + barcode));
        if (!book.isAvailable()) throw new RuntimeException("Book is already issued");
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setUser(user);
        record.setIssueDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14));
        record.setReturned(false);
        borrowRepo.save(record);
        
        book.setAvailable(false);
        bookRepo.save(book);
        
        emailService.sendEmail(user.getEmail(), "Book Issued", 
            "Dear " + user.getFullName() + ",\n\nYou have issued: " + book.getTitle() + 
            "\nDue date: " + record.getDueDate() + "\n\nLibrary System");
        
        return "Book issued successfully";
    }
    
    @Transactional
    public String returnBook(Long borrowId) {
        BorrowRecord record = borrowRepo.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        if (record.isReturned()) throw new RuntimeException("Book already returned");
        record.setReturned(true);
        record.setReturnDate(LocalDate.now());
        borrowRepo.save(record);
        
        Book book = record.getBook();
        book.setAvailable(true);
        bookRepo.save(book);
        return "Book returned successfully";
    }
    
    public List<BorrowRecord> getCurrentBorrowings(String username) {
        User user = userRepo.findByUsername(username).orElseThrow();
        return borrowRepo.findByUserAndReturnedFalse(user);
    }
}