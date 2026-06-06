package com.inventario.dao;

import com.inventario.model.Producto;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProductoDAOImpl.class);
    private List<Producto> baseDatos = new ArrayList<>();

    @Override
    public void agregarProducto(Producto p) {
        Preconditions.checkNotNull(p, "Error de seguridad: Producto nulo");
        String nombreSeguro = StringUtils.trimToEmpty(p.getNombre());
        
        if (nombreSeguro.isEmpty() || p.getCantidad() < 0 || p.getPrecio() < 0) {
            logger.warn("Intento de registro con datos inválidos o maliciosos.");
            throw new IllegalArgumentException("Datos inválidos. Verifique nombre, cantidad y precio.");
        }
        
        baseDatos.add(p);
        logger.info("Producto registrado en sistema: {}", nombreSeguro);
    }

    @Override
    public List<Producto> obtenerTodos() {
        return baseDatos;
    }
}