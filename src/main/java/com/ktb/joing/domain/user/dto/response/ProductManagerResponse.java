package com.ktb.joing.domain.user.dto.response;

import com.ktb.joing.domain.user.entity.Category;
import com.ktb.joing.domain.user.entity.FavoriteCategory;
import com.ktb.joing.domain.user.entity.ProductManager;

import java.util.List;

public record ProductManagerResponse(
        String nickname,
        String email,
        String profileImage,
        List<Category> favoriteCategories
) {
    public static ProductManagerResponse from(ProductManager productManager) {
        return new ProductManagerResponse(
                productManager.getNickname(),
                productManager.getEmail(),
                productManager.getProfileImage(),
                productManager.getFavoriteCategories().stream()
                        .map(FavoriteCategory::getCategory)
                        .toList()
        );
    }
}
