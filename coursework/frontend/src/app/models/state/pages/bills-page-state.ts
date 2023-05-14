import { BillDto } from "../../dto/bill/bill-dto";
import { OrderDto } from "../../dto/order/order-dto";
import { DataState } from "../enum/data-state";

export interface BillsPageState {
    bills?: BillDto[];
    dataState?: DataState;
    inactive?: boolean;
}