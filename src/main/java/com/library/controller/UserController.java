package com.library.controller;

import com.library.model.Book;
import com.library.repository.BookRepository;
import com.library.service.BorrowService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {
    private final BorrowService borrowService;
    private final BookRepository bookRepository;
    
    public UserController(BorrowService borrowService, BookRepository bookRepository) {
        this.borrowService = borrowService;
        this.bookRepository = bookRepository;
    }
    
    @GetMapping("/my-borrowings")
    public String myBorrowings(Model model, Authentication auth) {
        model.addAttribute("borrowings", borrowService.getCurrentBorrowings(auth.getName()));
        return "my-borrowings";
    }
    
    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            borrowService.returnBook(id);
            ra.addFlashAttribute("success", "Book returned!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/my-borrowings";
    }
    
    @GetMapping("/borrow")
    public String borrowForm(Model model) {
        model.addAttribute("books", bookRepository.findByAvailableTrue());
        return "borrow-book";
    }
    
    @PostMapping("/borrow/issue")
    public String issueManual(@RequestParam("bookId") Long bookId, Authentication auth, RedirectAttributes ra) {
        try {
            Book book = bookRepository.findById(bookId).orElseThrow();
            borrowService.issueBook(book.getBarcode(), auth.getName());
            ra.addFlashAttribute("success", "Book issued: " + book.getTitle());
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/borrow";
    }
}