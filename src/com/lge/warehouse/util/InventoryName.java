package com.lge.warehouse.util;

public enum InventoryName {
	INVENTORY_1,
	INVENTORY_2,
	INVENTORY_3,
	INVENTORY_4,
	INVENTORY_5;
        
        // Get InventoryName from int
        public static InventoryName fromInteger(int n) {
            switch(n) {
                case 0:
                    return INVENTORY_1;
                case 1:
                    return INVENTORY_2;    
                case 2:
                    return INVENTORY_3;
                case 3:
                    return INVENTORY_4;
                case 4:
                    return INVENTORY_5;
                default:
                    return INVENTORY_1;
            }
        }
        
        // Get int from InventoryName
        public static int fromInventoryName(InventoryName inventoryName) {
            switch(inventoryName) {
                case INVENTORY_1:
                    return 0;
                case INVENTORY_2:
                    return 1;
                case INVENTORY_3:
                    return 2;
                case INVENTORY_4:
                    return 3;
                case INVENTORY_5:
                    return 4;
                default:
                    return 0;
            }
        }
}
