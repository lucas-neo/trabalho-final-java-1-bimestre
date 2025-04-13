package com.unicesumar.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import com.unicesumar.paymentMethods.PaymentType;

public class InputHelper {

   public static String readValidEmail(Scanner scanner) {
    while (true) {
        System.out.print("Digite seu e-mail: ");
        String email = scanner.next().trim();

        if (email.isEmpty()) {
            System.out.println("O e-mail não pode estar vazio. Tente novamente.");
            continue;
        }

        String regex = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(regex)) {
            System.out.println("Formato de e-mail inválido. Tente novamente.");
            continue;
        }

        return email;
        }
    }



    public static List<UUID> readUUIDListFromString(Scanner scanner) {
    while (true) {
        System.out.print("Digite os UUIDs dos produtos separados por vírgula: ");
        String input = scanner.nextLine().trim();

        String[] parts = input.split(",");
        List<UUID> uuids = new ArrayList<>();

        boolean allValid = true;
        for (String part : parts) {
            try {
                UUID id = UUID.fromString(part.trim());
                uuids.add(id);
            } catch (IllegalArgumentException e) {
                System.out.println("UUID inválido encontrado: " + part.trim());
                allValid = false;
                break;
            }
        }

        if (allValid) {
            return uuids;
        } else {
            System.out.println("Por favor, digite todos os UUIDs corretamente.");
        }
    }
    }


    public static PaymentType readValidPaymentType(Scanner scanner) {
        while (true) {
            System.out.println("Escolha a forma de pagamento:");
            System.out.println("1 - PIX");
            System.out.println("2 - BOLETO");
            System.out.println("3 - CARTÃO");
            System.out.print("Opção: ");
            
            String opcao = scanner.nextLine().trim();
    
            switch (opcao) {
                case "1": return PaymentType.PIX;
                case "2": return PaymentType.BOLETO;
                case "3": return PaymentType.CARTAO;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
    
}
