package com.inventario.controller;

import com.inventario.dao.ProductoDAO;
import com.inventario.model.Producto;
import com.inventario.util.ExcelExporter;
import java.util.List;

// --- IMPORTACIONES DE GOOGLE GUAVA  ---
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class InventarioController {
    private ProductoDAO dao;

    public InventarioController(ProductoDAO dao) {
        this.dao = dao;
    }

    public void registrarProducto(String nombre, int cantidad, double precio) {
        // USO DE GUAVA: Validamos los datos de entrada de forma estricta
        Preconditions.checkArgument(!Strings.isNullOrEmpty(nombre), "El nombre no puede ser nulo o vacío");
        Preconditions.checkArgument(cantidad >= 0, "La cantidad no puede ser negativa");
        Preconditions.checkArgument(precio >= 0.0, "El precio no puede ser negativo");

        String idGenerado = String.valueOf(System.currentTimeMillis());
        Producto nuevo = new Producto(idGenerado, nombre, cantidad, precio);
        dao.agregarProducto(nuevo);
    }

    // --- NUEVO MÉTODO: ACTUALIZAR PRODUCTO ---
    public void actualizarProducto(String id, String nombre, int cantidad, double precio) {
        // Validación estricta con Guava
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "El ID no puede estar vacío");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(nombre), "El nombre no puede estar vacío");
        Preconditions.checkArgument(cantidad >= 0, "La cantidad no puede ser negativa");
        Preconditions.checkArgument(precio >= 0.0, "El precio no puede ser negativo");

        // Creamos el objeto actualizado y lo enviamos al DAO
        Producto productoActualizado = new Producto(id, nombre, cantidad, precio);
        dao.actualizarProducto(productoActualizado);
    }

    // --- NUEVO MÉTODO: ELIMINAR PRODUCTO ---
    public void eliminarProducto(String id) {
        // Validación con Guava
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "El ID no puede estar vacío para eliminar");

        // Enviamos la orden de eliminación al DAO
        dao.eliminarProducto(id);
    }

    public void exportarInventario() {
        List<Producto> lista = dao.obtenerTodos();
        ExcelExporter.exportar(lista, "Inventario_Reporte.xlsx");
    }
}
