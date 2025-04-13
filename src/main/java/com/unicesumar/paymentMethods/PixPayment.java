package com.unicesumar.paymentMethods;

import java.util.UUID;

public class PixPayment implements PaymentMethod {
    public void pay(double amount) {
        UUID chaveAutenticacao = UUID.randomUUID();
        System.out.printf("Pagamento via PIX de R$ %.2f realizado com sucesso.%n", amount);
        System.out.println("Chave de autenticação: " + chaveAutenticacao);
    }
}