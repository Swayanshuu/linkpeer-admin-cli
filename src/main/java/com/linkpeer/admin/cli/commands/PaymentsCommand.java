package com.linkpeer.admin.cli.commands;

import com.linkpeer.admin.domain.Payment;
import com.linkpeer.admin.repository.PaymentRepository;
import com.linkpeer.admin.service.AuthService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Command(name = "payments", description = "Payment management commands")
public class PaymentsCommand {

    private final PaymentRepository paymentRepository;
    private final AuthService authService;

    public PaymentsCommand(PaymentRepository paymentRepository, AuthService authService) {
        this.paymentRepository = paymentRepository;
        this.authService = authService;
    }

    private boolean checkAuth() {
        if (!authService.isAuthenticated()) {
            System.out.println("\u001B[31m✗ Please login first\u001B[0m");
            return false;
        }
        return true;
    }

    @Command(name = "recent", description = "List recent payments")
    public void recent() {
        if (!checkAuth()) return;
        List<Payment> payments = paymentRepository.findTop50ByOrderByCreatedAtDesc();
        printPayments(payments);
    }

    @Command(name = "pending", description = "List pending payments")
    public void pending() {
        if (!checkAuth()) return;
        List<Payment> payments = paymentRepository.findByStatusIgnoreCase("pending");
        printPayments(payments);
    }

    @Command(name = "view", description = "View payment details")
    public void view(@Parameters(index = "0", description = "Transaction ID or Payment UUID") String transactionId) {
        if (!checkAuth()) return;
        try {
            UUID id = UUID.fromString(transactionId);
            Optional<Payment> opt = paymentRepository.findById(id);
            if (opt.isPresent()) {
                Payment p = opt.get();
                System.out.println("ID: " + p.getId());
                System.out.println("Txn ID: " + p.getTransactionId());
                System.out.println("User: " + (p.getUser() != null ? p.getUser().getName() : ""));
                System.out.println("Plan Type: " + p.getPlanType());
                System.out.println("Amount: " + p.getAmount());
                System.out.println("Provider: " + p.getPaymentProvider());
                System.out.println("Status: " + p.getStatus());
                System.out.println("Date: " + p.getCreatedAt());
                return;
            }
        } catch (IllegalArgumentException ignored) {
        }

        System.out.println("\u001B[31m✗ Payment not found\u001B[0m");
    }

    private void printPayments(List<Payment> payments) {
        System.out.printf("%-36s | %-20s | %-10s | %-10s | %-10s | %-10s | %-20s%n",
                "Payment ID", "User", "Plan", "Amount", "Provider", "Status", "Date");
        System.out.println("-".repeat(130));
        for (Payment p : payments) {
            System.out.printf("%-36s | %-20s | %-10s | %-10s | %-10s | %-10s | %-20s%n",
                    p.getId(), (p.getUser() != null ? p.getUser().getName() : ""),
                    p.getPlanType(), p.getAmount(), p.getPaymentProvider(), p.getStatus(), p.getCreatedAt());
        }
    }
}
