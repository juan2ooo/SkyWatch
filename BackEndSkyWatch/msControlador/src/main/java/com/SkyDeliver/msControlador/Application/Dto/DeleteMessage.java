package com.SkyDeliver.msControlador.Application.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteMessage {


    private int idRuta;


    private int idDron;


    private String email;


    private byte[] foto; // Jackson convierte el Base64 a byte[] automáticamente

    // Getters y Setters obligatorios para que Spring pueda llenar el objeto
    public int getIdRuta() { return idRuta; }
    public void setIdRuta(int idRuta) { this.idRuta = idRuta; }

    public int getIdDron() { return idDron; }
    public void setIdDron(int idDron) { this.idDron = idDron; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public byte[] getFoto() { return foto; }
    public void setFoto(byte[] foto) { this.foto = foto; }
}
