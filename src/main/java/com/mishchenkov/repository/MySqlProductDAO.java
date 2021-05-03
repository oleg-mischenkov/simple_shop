package com.mishchenkov.repository;

import com.mishchenkov.dto.CategoryPaginationAndSortStateDTO;
import com.mishchenkov.dto.ProductDTO;
import com.mishchenkov.dto.ProductFilterFormDTO;
import com.mishchenkov.entity.Manufacture;
import com.mishchenkov.entity.Money;
import com.mishchenkov.entity.Product;
import com.mishchenkov.entity.ProductType;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class MySqlProductDAO implements ProductDAO {

    private static final Map<String,String> ORDER_BY_MAP;
    static {
        ORDER_BY_MAP = new HashMap<>();
        ORDER_BY_MAP.put("default", "ORDER BY products.id ");
        ORDER_BY_MAP.put("name_up", "ORDER BY products.title ");
        ORDER_BY_MAP.put("name_down", "ORDER BY products.title DESC ");
        ORDER_BY_MAP.put("price_up", "ORDER BY products.price ");
        ORDER_BY_MAP.put("price_down", "ORDER BY products.price DESC ");
    }

    private static final String SQL_SELECT_PRODUCT_COUNT_BY_FILTER = "SELECT count(products.id) FROM products "
            .concat("LEFT JOIN products_to_types ON id = products_to_types.products_id ")
            .concat("LEFT JOIN product_types ON product_types.id = products_to_types.product_types_id ")
            .concat("LEFT JOIN products_to_manufactures ON products.id = products_to_manufactures.products_id ")
            .concat("LEFT JOIN manufactures ON manufactures.id = products_to_manufactures.manufactures_id ");

    private static final String SQL_SELECT_COMMON_REQUEST = "SELECT products.id, products.sku, products.title, products.price, "
            .concat("product_types.`name` AS `type`, manufactures.`name` AS manufacture ")
            .concat("FROM products ")
            .concat("LEFT JOIN products_to_types ON id = products_to_types.products_id ")
            .concat("LEFT JOIN product_types ON product_types.id = products_to_types.product_types_id ")
            .concat("LEFT JOIN products_to_manufactures ON products.id = products_to_manufactures.products_id ")
            .concat("LEFT JOIN manufactures ON manufactures.id = products_to_manufactures.manufactures_id ");

    private static final String SQL_SELECT_MAX_PRICE = SQL_SELECT_COMMON_REQUEST
            .concat("WHERE products.price = (SELECT MAX(products.price) FROM products)");

    private static final String SQL_SELECT_PRODUCT_BY_ID = SQL_SELECT_COMMON_REQUEST
            .concat("WHERE products.id = ?");

    private static final String SQL_SELECT_PRODUCT_BY_SKU = SQL_SELECT_COMMON_REQUEST
            .concat("WHERE products.sku = ?");

    private final JDBCTemplate template;

    public MySqlProductDAO(JDBCTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<ProductDTO> selectMostExpensiveProduct(Connection connection) throws SQLException {
        return template.executeQuery(connection, SQL_SELECT_MAX_PRICE, rowMapper)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<List<ProductDTO>> selectProductsByFilter(Connection connection, ProductFilterFormDTO formDTO,
                                                             CategoryPaginationAndSortStateDTO sortPaginationDTO) throws SQLException {
        String sqlRequest = SQL_SELECT_COMMON_REQUEST.concat(addRequestConditionals(formDTO, sortPaginationDTO));

        return Optional.ofNullable(
                template.executeQuery(connection, sqlRequest, rowMapper, obtainRequestValues(formDTO, sortPaginationDTO))
        );
    }

    @Override
    public Optional<Integer> selectProductCountByFilter(Connection connection, ProductFilterFormDTO formDTO) throws SQLException {
        CategoryPaginationAndSortStateDTO sortPaginationDTO = new CategoryPaginationAndSortStateDTO();
        String sqlRequest = SQL_SELECT_PRODUCT_COUNT_BY_FILTER.concat(addRequestConditionals(formDTO, sortPaginationDTO));

        return template.executeQuery(connection, sqlRequest, countRowMapper, obtainRequestValues(formDTO, sortPaginationDTO))
                .stream()
                .findFirst();
    }

    @Override
    public Optional<ProductDTO> selectProductById(Connection connection, int productId) throws SQLException {
        return template.executeQuery(connection, SQL_SELECT_PRODUCT_BY_ID, rowMapper, productId).stream().findFirst();
    }

    @Override
    public Optional<ProductDTO> selectProductBySku(Connection connection, String sku) throws SQLException {
        return template.executeQuery(connection, SQL_SELECT_PRODUCT_BY_SKU, rowMapper, sku).stream().findFirst();
    }

    private Object[] obtainRequestValues(ProductFilterFormDTO formDTO, CategoryPaginationAndSortStateDTO sortPaginationDTO) {
        return Stream.of(
                formDTO.getMinPrice() == null ? null : Integer.parseInt(formDTO.getMinPrice()) * 100,
                formDTO.getMaxPrice() == null ? null : Integer.parseInt(formDTO.getMaxPrice()) * 100,
                formDTO.getProductTitle() == null ? null : formDTO.getProductTitle(),
                formDTO.getProductType() == null ? null : formDTO.getProductType(),
                formDTO.getManufacture() == null ? null : formDTO.getManufacture(),
                sortPaginationDTO.getCount() == null ? null : Integer.parseInt(sortPaginationDTO.getCount()),
                sortPaginationDTO.getPosition() == null ? null : Integer.parseInt(sortPaginationDTO.getPosition())
        )
                .filter(Objects::nonNull)
                .toArray();
    }

    private String addRequestConditionals(ProductFilterFormDTO formDTO, CategoryPaginationAndSortStateDTO sortPaginationDTO) {
        return new StringBuilder()
                .append(!isFormEmpty(formDTO) ? "WHERE " : "")
                .append(formDTO.getMinPrice() == null ? "" : "products.price >= ? AND products.price <= ? ")
                .append(formDTO.getProductTitle() == null ? "" : "AND products.title like(concat('%', ?, '%')) ")
                .append(formDTO.getProductType() == null ? "" : "AND product_types.`name` = ? ")
                .append(formDTO.getManufacture() == null ? "" : "AND manufactures.`name` = ? ")
                .append(sortPaginationDTO.getSortBy() == null ? "" : ORDER_BY_MAP.get(sortPaginationDTO.getSortBy()))
                .append(sortPaginationDTO.getCount() == null ? "" : "LIMIT ? OFFSET ? ")
                .toString();
    }

    private <T> boolean isFormEmpty(T someDTO) {
        return Arrays.stream(someDTO.getClass().getDeclaredFields())
                .noneMatch(field -> {
                    field.setAccessible(true);
                    return isFieldHasType(field, String.class) && isFieldNull(someDTO, field);
                });
    }

    private <T> boolean isFieldHasType(Field field, Class<T> clazz) {
        return field.getType().equals(clazz);
    }

    private <T> boolean isFieldNull(T someDTO, Field field) {
        boolean result = false;
        try {
            result = field.get(someDTO) != null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    private final RowMapper<Integer> countRowMapper = resultSet -> resultSet.getInt(1);

    private final RowMapper<ProductDTO> rowMapper = resultSet ->
            new ProductDTO()
                    .setKey(resultSet.getLong("id"))
                    .setProduct(Product.getBuilder()
                            .setTitle(resultSet.getString("title"))
                            .setSku(resultSet.getString("sku"))
                            .setPrice(new Money(resultSet.getInt("price")))
                            .setProductType(new ProductType(resultSet.getString("type")))
                            .setManufacture(new Manufacture(resultSet.getString("manufacture")))
                            .build()
                    );
}
