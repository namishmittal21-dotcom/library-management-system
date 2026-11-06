package com.library.controller;

import com.library.service.BorrowService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/barcode")
public class BarcodeController {
    private final BorrowService borrowService;
    public BarcodeController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }
    
    @GetMapping("/issue")
    public String barcodeIssuePage() {
        return "barcode-issue";
    }
    
    @PostMapping("/scan")
    public String processBarcode(@RequestParam String barcode, Authentication auth, RedirectAttributes ra) {
        try {
            borrowService.issueBook(barcode, auth.getName());
            ra.addFlashAttribute("success", "Book issued successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/barcode/issue";
    }
}