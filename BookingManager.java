/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ticktbookingsystem;
public class BookingManager {
  

    private final IPaymentGatway paymentGateway;
    private final INotificationServiec notificationService;
    private final IEventRepository eventRepository;

    public BookingManager(IPaymentGatway paymentGateway, 
                          INotificationServiec notificationService, 
                          IEventRepository eventRepository) {
        this.paymentGateway = paymentGateway;
        this.notificationService = notificationService;
        this.eventRepository = eventRepository;
    }

    public boolean bookTicket(String eventId, double amount) {
        if (eventId == null || amount <= 0) {
            return false;
        }

        if (eventRepository.isSoldOut(eventId)) {
            return false;
        }

        String transactionId = paymentGateway.processPayment(amount);
        
        if (transactionId != null) {
            eventRepository.saveBooking(eventId, transactionId);
            notificationService.sendConfirmation("Booking confirmed for event: " + eventId);
            return true;
        }

        return false;
    }
}

