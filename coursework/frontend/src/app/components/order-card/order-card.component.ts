import { Component, EventEmitter, Input, Output } from '@angular/core';
import { OrderDto } from 'src/app/models/dto/order/order-dto';
import { TableDto } from 'src/app/models/dto/table/table-dto';
import { ObjectUtils } from 'src/app/models/util/object-utils';

@Component({
  selector: 'app-order-card',
  templateUrl: './order-card.component.html',
  styleUrls: ['./order-card.component.css']
})
export class OrderCardComponent {
  @Input() order: OrderDto;
  @Input() table: TableDto;

  @Output() onClick = new EventEmitter<string>();

  constructor() { }

  cardClick() {
    this.onClick.emit(this.order.id);
  }

  dateString(): string {
    return ObjectUtils.formatDate(this.order.createdAt);
  }
}

