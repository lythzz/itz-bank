/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itz.bank;

/**
 *
 * @author lucas
 */
public class Cliente {
    String cpf;
    String nome;
    String username;
    String senha;
    
    ContaCorrente contaCorrente;
    Conta contaInvestimento;
    
    public Cliente(String cpf, String nome, String username, String senha, ContaCorrente corrente){
        this.cpf = cpf;
        this.nome = nome;
        this.username = username;
        this.senha = senha;
        this.contaCorrente = corrente;
    }
}