package it.unifi.escapemanager.controllers;

import it.unifi.escapemanager.dao.WaitingListDAO;
import it.unifi.escapemanager.exceptions.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Mock Test per ListaAttesaController.
 * Verifica la logica di iscrizione alla lista d'attesa (UC2) in isolamento dal DB.
 */
@ExtendWith(MockitoExtension.class)
class ListaAttesaControllerMockTest {

    @Mock
    private WaitingListDAO waitingListDAO;

    private ListaAttesaController controller;

    @BeforeEach
    void setUp() {
        controller = new ListaAttesaController(waitingListDAO);
    }

    @Test
    void testIscrizioneRiuscita_UC2_FlussoBase() {
        when(waitingListDAO.exists("C01", "R01")).thenReturn(false);

        assertDoesNotThrow(() -> controller.iscrivi("C01", "R01"));

        verify(waitingListDAO).add(eq("C01"), eq("R01"), any(LocalDateTime.class));
    }

    @Test
    void testIscrizioneDuplicata_UC2_Alt5a() {
        when(waitingListDAO.exists("C01", "R01")).thenReturn(true);

        assertThrows(ValidationException.class, () -> controller.iscrivi("C01", "R01"));

        // Verify: add NON è stato chiamato
        verify(waitingListDAO, never()).add(any(), any(), any());
    }
}
