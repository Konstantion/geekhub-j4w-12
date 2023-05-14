import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BillDto } from 'src/app/models/dto/bill/bill-dto';

@Component({
  selector: 'app-bill-card',
  templateUrl: './bill-card.component.html',
  styleUrls: ['./bill-card.component.css']
})
export class BillCardComponent {
  @Input() bill: BillDto;
  @Output() onClick = new EventEmitter<string>();

  cardClick() {
    this.onClick.emit(this.bill.id);
  }
}
