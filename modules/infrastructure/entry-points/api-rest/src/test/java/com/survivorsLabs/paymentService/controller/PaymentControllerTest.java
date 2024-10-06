package com.survivorsLabs.paymentService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.survivorsLabs.paymentService.dto.TransactionDTO;
import com.survivorsLabs.paymentService.model.Transaction;
import com.survivorsLabs.paymentService.model.TransactionResponse;
import com.survivorsLabs.paymentService.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPay() throws Exception {
        // Datos de prueba
        TransactionDTO transaction = new TransactionDTO();

        Mockito.when(paymentUseCase.processPayment(ArgumentMatchers.any(Transaction.class))).thenReturn(TransactionResponse.builder().build());

        // Envío de la solicitud POST
        mockMvc.perform(MockMvcRequestBuilders.post("/survivors-labs-api/lite_payment_service/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verificación de que el servicio se haya llamado una vez con los datos correctos
        Mockito.verify(paymentUseCase, Mockito.times(1)).processPayment(Transaction.builder().build());
    }


}
