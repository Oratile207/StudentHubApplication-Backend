package za.co.studenthub.factory;

import za.co.studenthub.domain.Products;
import za.co.studenthub.domain.UserProduct;
import za.co.studenthub.domain.enums.ReturnType;
import za.co.studenthub.util.Helper;

public class ProductFactory {
    public static Products createProduct(String productName,
                                         String productDescription,
                                         ReturnType returnType,
                                         UserProduct userProduct) {

        if (Helper.isNullOrEmpty(productName) ||
                Helper.isNullOrEmpty(productDescription) ||
                returnType == null ||
                userProduct == null) {
            return null;
        }

        return Products.builder()
                .productName(productName)
                .productDescription(productDescription)
                .productReturnType(returnType)
                .userProduct(userProduct)
                .build();
    }
}