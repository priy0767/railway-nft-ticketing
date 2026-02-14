package com.railway.nftticketing;

import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;

@RestController
@RequestMapping("/api/tickets") // <--- This matches your frontend!
public class TicketController {

    private final TicketService ticketService;

    // Connects the Controller to your Logic (Service)
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // 1. Endpoint to Book a Ticket
    // URL: /api/tickets/book?passengerName=...&trainId=...
    @PostMapping("/book")
    public String bookTicket(
            @RequestParam String passengerName,
            @RequestParam String trainId,
            @RequestParam String date,
            @RequestParam String seatNumber
    ) {
        try {
            return ticketService.bookTicket(passengerName, trainId, date, seatNumber);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // 2. Endpoint to Verify a Ticket
    // URL: /api/tickets/verify/1
    @GetMapping("/verify/{ticketId}")
    public String verifyTicket(@PathVariable BigInteger ticketId) {
        try {
            return ticketService.verifyTicket(ticketId);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}