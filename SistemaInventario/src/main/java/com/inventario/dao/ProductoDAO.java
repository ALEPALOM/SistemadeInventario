package com.inventario.dao;
import com.inventario.model.Producto;
import java.util.List;

public interface ProductoDAO {
    void agregarProducto(Producto p);
    List<Producto> obtenerTodos();
}