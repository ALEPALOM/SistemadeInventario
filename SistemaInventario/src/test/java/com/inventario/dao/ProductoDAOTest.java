package com.inventario.dao;

import com.inventario.model.Producto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoDAOTest {

    @Test
    public void testAgregarProductoExitoso() {
        ProductoDAO dao = new ProductoDAOImpl();
        Producto p = new Producto("1", "Laptop", 10, 1500.0);
        dao.agregarProducto(p);
        
        assertEquals(1, dao.obtenerTodos().size());
        assertEquals("Laptop", dao.obtenerTodos().get(0).getNombre());
    }

    @Test
    public void testAgregarProductoInvalidoLanzaExcepcion() {
        ProductoDAO dao = new ProductoDAOImpl();
        Producto p = new Producto("2", "  ", -5, 10.0); // Nombre vacío, cantidad negativa
        
        assertThrows(IllegalArgumentException.class, () -> {
            dao.agregarProducto(p);
        });
    }
}