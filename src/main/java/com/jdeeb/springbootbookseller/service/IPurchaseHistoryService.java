package com.jdeeb.springbootbookseller.service;

import com.jdeeb.springbootbookseller.model.PurchaseHistory;
import com.jdeeb.springbootbookseller.repository.projection.IPurchaseItem;

import java.util.List;

public interface IPurchaseHistoryService {
    PurchaseHistory savePurchaseHistory(PurchaseHistory purchaseHistory);

    List<IPurchaseItem> findPurchasedItemsOfUser(Long userId);
}
