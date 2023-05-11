import { Data } from "@angular/router";

export interface OrderDto {
    id: string;
    active: boolean;
    productsId: string[];
    tableId: string;
    userId: string;
    billId: string;
    createdAt: Data;
    closedAt: Date;
}