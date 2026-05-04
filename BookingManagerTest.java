/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ticktbookingsystem;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BookingManagerTest {

    private BookingManager bookingManager;

    @Mock private IPaymentGatway paymentGateway;
    @Mock private INotificationServiec notificationService;
    @Mock private IEventRepository eventRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingManager = new BookingManager(paymentGateway, notificationService, eventRepository);
    }

    @Test
    void testBookingSuccess_US01() {
        when(eventRepository.isSoldOut("E1")).thenReturn(false);
        when(paymentGateway.processPayment(100.0)).thenReturn("TX123");

        boolean result = bookingManager.bookTicket("E1", 100.0);

        assertTrue(result);
        verify(eventRepository, times(1)).saveBooking("E1", "TX123");
        verify(notificationService, times(1)).sendConfirmation(anyString());
    }

    @Test
    void testInvalidInput_US02() {
        boolean result = bookingManager.bookTicket(null, -50.0);

        assertFalse(result);
        verify(paymentGateway, never()).processPayment(anyDouble());
    }

    @Test
    void testSoldOut_US03() {
        when(eventRepository.isSoldOut("E1")).thenReturn(true);

        boolean result = bookingManager.bookTicket("E1", 100.0);

        assertFalse(result);
        verify(paymentGateway, never()).processPayment(anyDouble());
    }
}

