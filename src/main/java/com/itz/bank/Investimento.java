/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itz.bank;
import java.time.LocalDateTime;
/**
 *
 * @author lucas
 */
public class Investimento {
    long id;
    int tipoNum;
    String tipo;
    float aporte;
    float rentabilidadeMinuto;
    int liquidez;
    LocalDateTime data;
    LocalDateTime prazo;
    
    public Investimento(String tipo, float aporte, LocalDateTime data){
        this.aporte = aporte;
        this.data = data;
        this.id = System.currentTimeMillis();
        
        switch(tipo){
            case "1": // LCI
                this.tipoNum = 1;
                this.tipo = "LCI";
                this.liquidez = 275;
                this.rentabilidadeMinuto = 0.00027f;
                this.prazo = LocalDateTime.now().plusMinutes(275);
                break;
            case "2": //LCA
                this.tipoNum = 2;
                this.tipo = "LCA";
                this.liquidez = 180;
                this.rentabilidadeMinuto = 0.00022f;
                this.prazo = LocalDateTime.now().plusMinutes(180);
                break;
            case "3": // CDB
                this.tipoNum = 3;
                this.tipo = "CDB";
                this.liquidez = 0;
                this.rentabilidadeMinuto = 0.0003f;
                this.prazo = LocalDateTime.now();
                break;
        }
    }
}
