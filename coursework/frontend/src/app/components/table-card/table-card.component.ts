import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { TableDto } from 'src/app/models/dto/table/table-dto';

@Component({
  selector: 'app-table-card',
  templateUrl: './table-card.component.html',
  styleUrls: ['./table-card.component.css']
})
export class TableCardComponent {
  @Input() table: TableDto;

  constructor(
    private router: Router
  ) { }

  onCardClick() {
    console.log(this.table.id);
    this.router.navigate([`tables/${this.table.id}`]);
  }
}
