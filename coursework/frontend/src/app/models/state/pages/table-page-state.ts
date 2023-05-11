import { HallDto } from "../../dto/hall/hall-dto";
import { OrderDto } from "../../dto/order/order-dto";
import { TableDto } from "../../dto/table/table-dto";
import { UserDto } from "../../dto/user/user-dto";
import { DataState } from "../enum/data-state";

export interface TablePageState {
    table?: TableDto;
    tableState?: DataState;
    users?: UserDto[];
    usersState?: DataState;
    message?: string;
    waiters?: UserDto[];
}