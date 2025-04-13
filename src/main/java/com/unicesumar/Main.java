package com.unicesumar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import com.unicesumar.entities.Product;
import com.unicesumar.entities.User;
import com.unicesumar.paymentMethods.PaymentType;
import com.unicesumar.repository.ProductRepository;
import com.unicesumar.repository.UserRepository;
import com.unicesumar.utils.InputHelper;
import com.unicesumar.entities.Venda;
import com.unicesumar.repository.VendaRepository;


public class Main {
    public static void main(String[] args) {
        ProductRepository listaDeProdutos = null;
        UserRepository listaDeUsuarios = null;

        Connection conn = null;

        // Parâmetros de conexão
        String url = "jdbc:sqlite:database.sqlite";

        // Tentativa de conexão
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                listaDeProdutos = new ProductRepository(conn);
                listaDeUsuarios = new UserRepository(conn);
            } else {
                System.out.println("Falha na conexão.");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            System.exit(1);
        }

        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n---MENU---");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Listas Produtos");
            System.out.println("3 - Cadastrar Usuário");
            System.out.println("4 - Listar Usuários");
            System.out.println("5 - Sair");
            System.out.println("6 - Registrar Venda");
            System.out.println("Escolha uma opção: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Cadastrar Produto");
                    System.out.println("Nome do Produto: ");
                    String nomeProduto = scanner.next();
                    System.out.println("Preço do Produto: ");
                    Double precoProduto = scanner.nextDouble();

                    listaDeProdutos.save(new Product(nomeProduto, precoProduto));
                    break;

                case 2:
                    System.out.println("Listar Produtos");
                    List<Product> products = listaDeProdutos.findAll();
                    products.forEach(System.out::println);
                    break;
                    
                case 3:
                    System.out.println("Cadastrar Usuário");
                    System.out.println("Nome:");
                    String nome = scanner.next();
                    String email = InputHelper.readValidEmail(scanner);
                    System.out.println("Senha:");
                    String senha = scanner.next();
                    listaDeUsuarios.save(new User(nome, email, senha));
                    break;

                case 4:
                    System.out.println("Listar Usuários");
                    List<User> users = listaDeUsuarios.findAll();
                    users.forEach(System.out::println);
                    break;

                case 5:
                    System.out.println("Saindo...");
                    break;

                case 6:
                System.out.println("Registrar Venda");

                scanner.nextLine(); // limpar buffer pendente
                String emailUsuario = InputHelper.readValidEmail(scanner);
            
                Optional<User> optionalUser = listaDeUsuarios.findByEmail(emailUsuario);
                if (optionalUser.isEmpty()) {
                    System.out.println("Usuário não encontrado.");
                    break;
                }
            
                User user = optionalUser.get();
                System.out.println("Usuário encontrado: " + user.getName());
            
                List<UUID> uuids = InputHelper.readUUIDListFromString(scanner);
                List<Product> produtosSelecionados = new LinkedList<>();
            
                for (UUID uuid : uuids) {
                    listaDeProdutos.findById(uuid).ifPresentOrElse(
                        produtosSelecionados::add,
                        () -> System.out.println("Produto com ID " + uuid + " não encontrado.")
                    );
                }
            
                if (produtosSelecionados.isEmpty()) {
                    System.out.println("Nenhum produto válido selecionado. Cancelando venda.");
                    break;
                }
            
                PaymentType paymentType = InputHelper.readValidPaymentType(scanner);
            
                Venda venda = new Venda(user, produtosSelecionados, paymentType);
                System.out.println("\nResumo da venda:");
                System.out.println(venda);
            
                PaymentManager manager = new PaymentManager();
                manager.setPaymentMethod(PaymentMethodFactory.create(paymentType));
                manager.pay(venda.getTotal());
            
                VendaRepository vendaRepository = new VendaRepository(conn);
                vendaRepository.save(venda);
            
                break;

                default:
                    System.out.println("Opção inválida. Tente novamente");

            }

        } while (option != 5);

        scanner.close();
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
