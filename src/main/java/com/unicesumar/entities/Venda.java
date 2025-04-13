package com.unicesumar.entities;

import java.util.List;

import com.unicesumar.paymentMethods.PaymentType;

public class Venda extends Entity{
    private User user;
    private List<Product> produtos;
    private PaymentType paymentMethod;

    public Venda(User user, List<Product> products, PaymentType paymentMethod) {
        super();
        this.user = user;
        this.produtos = products;
        this.paymentMethod = paymentMethod;
    }

    public User getUser() {
        return user;
    }

    public List<Product> getProdutos() {
        return produtos;
    }

    public PaymentType getPaymentMethod() {
        return paymentMethod;
    }

    public double getTotal() {
        return produtos.stream().mapToDouble(Product::getPrice).sum();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Cliente: ").append(user.getName()).append("\n");
        builder.append("Produtos:\n");
        produtos.forEach(p -> builder.append("- ").append(p.getName())
                                     .append(" (R$ ").append(p.getPrice()).append(")\n"));
        builder.append("Valor total: R$ ").append(getTotal()).append("\n");
        builder.append("Pagamento: ").append(paymentMethod);
        return builder.toString();
    }

}
