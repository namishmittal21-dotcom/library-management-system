package com.library.scheduler;

import com.library.model.BorrowRecord;
import com.library.repository.BorrowRecordRepository;
import com.library.service.EmailService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
@EnableScheduling
public class DueDateReminderScheduler {
    private final BorrowRecordRepository borrowRepo;
    private final EmailService emailService;
    
    public DueDateReminderScheduler(BorrowRecordRepository borrowRepo, EmailService emailService) {
        this.borrowRepo = borrowRepo;
        this.emailService = emailService;
    }
    
    @Scheduled(cron = "0 0 9 * * *") // daily at 9 AM
    public void sendDueDateReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<BorrowRecord> records = borrowRepo.findByReturnedFalseAndDueDateBefore(tomorrow);
        for (BorrowRecord record : records) {
            emailService.sendEmail(record.getUser().getEmail(), "Due Date Reminder",
                "Dear " + record.getUser().getFullName() + ",\nBook '" + record.getBook().getTitle() +
                "' is due on " + record.getDueDate() + ". Please return on time.");
        }
    }
}