package ru.avalon.java.j30.labs;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Collection;
import java.util.Properties;

/**
 * Лабораторная работа №3
 * <p>
 * Курс: "DEV-OCPJP. Подготовка к сдаче сертификационных экзаменов серии Oracle Certified Professional Java Programmer"
 * <p>
 * Тема: "JDBC - Java Database Connectivity" 
 *
 * @author Daniel Alpatov <danial.alpatov@gmail.com>
 */
public class Main {

    /**
     * Точка входа в приложение
     * 
     * @param args the command line arguments
     */
    public static String PARAM = "resources/dbproperties.properties";
    public static void main(String[] args) throws SQLException {
        
        try (Connection connection = getConnection()) {
            ProductCode code = new ProductCode("MO", 'N', "Movies");
            
//             String query = "select * from APP.PRODUCT_CODE";
//            PreparedStatement statement = connection.prepareStatement(query);
//          //  statement.setString(1, "%ing%");
//            ResultSet resultSet = statement.executeQuery();
//            code.convert(resultSet);
                    
            code.save(connection);
            printAllCodes(connection);

            code.setCode("MV");
            code.save(connection);
            printAllCodes(connection);
        }
        /*
         * TODO #14 Средствами отладчика проверьте корректность работы программы
         */
    }
    /**
     * Выводит в кодсоль все коды товаров
     * 
     * @param connection действительное соединение с базой данных
     * @throws SQLException 
     */    
    private static void printAllCodes(Connection connection) throws SQLException {
        Collection<ProductCode> codes = ProductCode.all(connection);
        for (ProductCode code : codes) {
            System.out.println(code);
        }
    }
    /**
     * Возвращает URL, описывающий месторасположение базы данных
     * 
     * @return URL в виде объекта класса {@link String}
     */
    private static String getUrl() {
        StringBuilder url = new StringBuilder();
        url.append("jdbc:")
                .append(getProperties().get("database.driver"))
                .append("://")
                .append(getProperties().get("database.host"))
                .append(":")               
                .append(getProperties().get("database.port"))
                .append("/")               
                .append(getProperties().get("database.name"));
        return url.toString();                
    }
    /**
     * Возвращает параметры соединения
     * 
     * @return Объект класса {@link Properties}, содержащий параметры user и 
     * password
     */
    private static Properties getProperties() {
        Properties properties = new Properties();
        try(InputStream stream = ClassLoader.getSystemResourceAsStream(PARAM)){
            properties.load(stream);
        } catch(IOException e){
            throw new IllegalStateException("Database not found!");
        }
        return properties;
    }
    /**
     * Возвращает соединение с базой данных Sample
     * 
     * @return объект типа {@link Connection}
     * @throws SQLException 
     */
    private static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(getUrl(),
                                              getProperties().getProperty("database.user"), 
                                              getProperties().getProperty("database.password"));
        return connection;
    }
    
}
