package com.example.juan.concesionario;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Vehiculo {
    int id;
    String marca;
    String modelo;
    byte [] imagenBytes;
    Bitmap imagenBitmap;
    double precio;
    String descripcion;
    boolean nuevo;

    public Vehiculo(int id, String marca, String modelo, byte[] imagenBytes, double precio, String descripcion, int nuevo) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.imagenBytes = imagenBytes;
        this.imagenBitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
        this.precio = precio;
        this.descripcion = descripcion;
        if(nuevo == 1)
        {
            this.nuevo = true;
        }
        else
        {
            this.nuevo = false;
        }
    }

    public Vehiculo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public byte[] getImagenBytes() {
        return imagenBytes;
    }

    public void setImagenBytes(byte[] imagenBytes) {
        this.imagenBytes = imagenBytes;
    }

    public Bitmap getImagenBitmap() {
        return imagenBitmap;
    }

    public void setImagenBitmap(Bitmap imagenBitmap) {
        this.imagenBitmap = imagenBitmap;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
