package com.library.controller;

import com.library.model.Book;
import com.library.model.User;
import com.library.model.Role;
import com.library.model.BorrowRecord;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import com.library.repository.BorrowRecordRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private final BorrowRecordRepository borrowRecordRepo;
    private final PasswordEncoder encoder;

    public AdminController(BookRepository bookRepo, UserRepository userRepo, 
                           BorrowRecordRepository borrowRecordRepo, PasswordEncoder encoder) {
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
        this.borrowRecordRepo = borrowRecordRepo;
        this.encoder = encoder;
    }

    // ==================== BOOK MANAGEMENT ====================
    
    @GetMapping("/books")
    public String manageBooks(Model model) {
        model.addAttribute("books", bookRepo.findAll());
        model.addAttribute("newBook", new Book());
        return "books";
    }

    @PostMapping("/books/save")
    public String saveBook(@ModelAttribute Book book, RedirectAttributes ra) {
        if (book.getBarcode() == null || book.getBarcode().trim().isEmpty()) {
            ra.addFlashAttribute("error", "Barcode is required!");
            return "redirect:/admin/books";
        }
        
        // Check for duplicate barcode (only for new books)
        if (book.getId() == null && bookRepo.findByBarcode(book.getBarcode()).isPresent()) {
            ra.addFlashAttribute("error", "Book with barcode " + book.getBarcode() + " already exists!");
            return "redirect:/admin/books";
        }
        
        bookRepo.save(book);
        ra.addFlashAttribute("success", "Book added/updated successfully!");
        return "redirect:/admin/books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes ra) {
        Book book = bookRepo.findById(id).orElse(null);
        if (book == null) {
            ra.addFlashAttribute("error", "Book not found!");
            return "redirect:/admin/books";
        }
        
        // Check if book has any active borrow records (not returned)
        List<BorrowRecord> activeBorrowRecords = borrowRecordRepo.findByBookAndReturnedFalse(book);
        if (!activeBorrowRecords.isEmpty()) {
            ra.addFlashAttribute("error", "Cannot delete '" + book.getTitle() + "'! It has " + 
                                 activeBorrowRecords.size() + " active borrowing(s) that haven't been returned.");
            return "redirect:/admin/books";
        }
        
        // Also check for any borrow records (even returned ones) - optional
        // If you want to prevent deletion if book was ever borrowed, uncomment below:
        /*
        List<BorrowRecord> allBorrowRecords = borrowRecordRepo.findByBook(book);
        if (!allBorrowRecords.isEmpty()) {
            ra.addFlashAttribute("error", "Cannot delete '" + book.getTitle() + "'! It has borrowing history.");
            return "redirect:/admin/books";
        }
        */
        
        bookRepo.deleteById(id);
        ra.addFlashAttribute("success", "Book '" + book.getTitle() + "' deleted successfully!");
        return "redirect:/admin/books";
    }

    // ==================== MEMBER MANAGEMENT ====================

    @GetMapping("/members")
    public String manageMembers(Model model) {
        List<User> members = userRepo.findAll().stream()
                .filter(u -> u.getRole() == Role.ROLE_USER)
                .toList();
        model.addAttribute("members", members);
        model.addAttribute("newMember", new User());
        return "members";
    }

    @PostMapping("/members/save")
    public String saveMember(@ModelAttribute User user, RedirectAttributes ra) {
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            ra.addFlashAttribute("error", "Username '" + user.getUsername() + "' already exists!");
            return "redirect:/admin/members";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepo.save(user);
        ra.addFlashAttribute("success", "Member '" + user.getFullName() + "' added successfully!");
        return "redirect:/admin/members";
    }

    @GetMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable("id") Long id, RedirectAttributes ra) {
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            ra.addFlashAttribute("error", "Member not found!");
            return "redirect:/admin/members";
        }
        
        // Check if user has active borrowings
        List<BorrowRecord> activeBorrowings = borrowRecordRepo.findByUserAndReturnedFalse(user);
        if (!activeBorrowings.isEmpty()) {
            ra.addFlashAttribute("error", "Cannot delete member '" + user.getFullName() + "'! They have " + 
                                 activeBorrowings.size() + " active book(s) to return.");
            return "redirect:/admin/members";
        }
        
        userRepo.deleteById(id);
        ra.addFlashAttribute("success", "Member '" + user.getFullName() + "' deleted successfully!");
        return "redirect:/admin/members";
    }
}