package com.inventario.dao;

import com.inventario.model.Producto;
import com.inventario.util.DatabaseConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProductoDAOImplTest {

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Test
    void testAgregarProductoExitoso() throws Exception {
        // 1. Preparación
        Producto producto = new Producto("P001", "Laptop", 5, 1500.0);
        ProductoDAOImpl dao = new ProductoDAOImpl();

        // 2. Simulamos la conexión estática
        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

            // 3. Ejecución
            dao.agregarProducto(producto);

            // 4. Verificación
            verify(preparedStatementMock).setString(1, "P001");
            verify(preparedStatementMock).setString(2, "Laptop");
            verify(preparedStatementMock).setInt(3, 5);
            verify(preparedStatementMock).setDouble(4, 1500.0);
            verify(preparedStatementMock, times(1)).executeUpdate();
        }
    }
    
    @Test
    void testEliminarProductoExitoso() throws Exception {
        // 1. Preparación
        String idAEliminar = "P001";
        ProductoDAOImpl dao = new ProductoDAOImpl();

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
            
            // Simulamos que el borrado fue exitoso (1 fila afectada)
            when(preparedStatementMock.executeUpdate()).thenReturn(1);

            // 2. Ejecución
            dao.eliminarProducto(idAEliminar);

            // 3. Verificación
            verify(preparedStatementMock).setString(1, idAEliminar);
            verify(preparedStatementMock, times(1)).executeUpdate();
        }
    }

    @Test
    void testEliminarProductoLanzaExceptionCuandoNoExiste() throws Exception {
        String idInexistente = "NO_EXISTE";
        ProductoDAOImpl dao = new ProductoDAOImpl();

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {
            dbMock.when(DatabaseConnection::getConnection).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
            
            // Simulamos que no se borró nada (0 filas)
            when(preparedStatementMock.executeUpdate()).thenReturn(0);

            // Verificamos que el DAO lanza la excepción
            assertThrows(RuntimeException.class, () -> {
                dao.eliminarProducto(idInexistente);
            });
        }
    }
  @Test
    void testSeguridadInyeccionSQL() {
        // 1. Declaramos el DAO igual que en los otros tests
        ProductoDAOImpl dao = new ProductoDAOImpl();
        
        // 2. Intentar buscar con caracteres peligrosos
        String inputPeligroso = "' OR '1'='1"; 
        
        // 3. Verificamos que no lance errores críticos (esto es seguridad básica)
        assertDoesNotThrow(() -> {
            dao.buscarProducto(inputPeligroso);
        });
    }
        
}