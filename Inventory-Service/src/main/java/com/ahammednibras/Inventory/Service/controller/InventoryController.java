package com.ahammednibras.Inventory.Service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @GetMapping("/{productId}")
    public int getStock(@PathVariable Long productId) {
        // Dummy logic for inventory; you can return a random stock value for now.
        return (int) (Math.random() * 100);
    }
}
