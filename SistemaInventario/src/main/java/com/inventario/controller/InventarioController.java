package com.inventario.controller;

import com.inventario.dao.ProductoDAO;
import com.inventario.model.Producto;
import com.inventario.util.ExcelExporter;
import java.util.List;

public class InventarioController {
    private ProductoDAO dao;

    public InventarioController(ProductoDAO dao) {
        this.dao = dao;
    }

    public void registrarProducto(String nombre, int cantidad, double precio) {
        String idGenerado = String.valueOf(System.currentTimeMillis());
        Producto nuevo = new Producto(idGenerado, nombre, cantidad, precio);
        dao.agregarProducto(nuevo);
    }

    public void exportarInventario() {
        List<Producto> lista = dao.obtenerTodos();
        ExcelExporter.exportar(lista, "Inventario_Reporte.xlsx");
    }
}
