package com.example.looklost;

public class ItemModel {
    private String itemId,userId;
    private String itemName;
    private String itemCategory;
    private String itemLocation;
    private String itemDescription;
    private String itemImage;


    public ItemModel(String itemId, String userId, String itemName, String itemCategory, String itemLocation, String itemDescription, String itemImage) {
        this.itemId = itemId;
        this.userId = userId;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemLocation = itemLocation;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
    }

    public ItemModel() {
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
