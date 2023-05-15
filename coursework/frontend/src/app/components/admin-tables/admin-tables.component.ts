import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { BehaviorSubject, catchError, map, of } from 'rxjs';
import { DataState } from 'src/app/models/state/enum/data-state';
import { TablesPageState } from 'src/app/models/state/pages/tables-page-state';
import { TableService } from 'src/app/services/table/table.service';

@Component({
  selector: 'app-admin-tables',
  templateUrl: './admin-tables.component.html',
  styleUrls: ['./admin-tables.component.css'],
  providers: [ConfirmationService, MessageService]
})
export class AdminTablesComponent implements OnInit {
  constructor(
    private router: Router,
    private tableService: TableService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) { }


  private tablesPageSubject = new BehaviorSubject<TablesPageState>({ tablesState: DataState.LOADING_STATE });

  pageState$ = this.tablesPageSubject.asObservable();

  onlyInactive = false;

  readonly DataState = DataState;

  ngOnInit(): void {
    this.tableService.tables$.pipe(
      map(response => {
        this.tablesPageSubject.next({ tablesState: DataState.LOADED_STATE, tables: response.data.tables });
      }),
      catchError(error => this.handleError(error))
    ).subscribe();
  }

  handleError(error: any) {
    let errorResponse = error.error;
    if (error.status === 403) {
      this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'Not enough authorities' });
    } else if (error.status === 400) {
      this.messageService.add({ severity: 'error', summary: 'Rejected', detail: errorResponse.message });
    } else {
      this.messageService.add({ severity: 'error', summary: 'Rejected', detail: error.message });
    }
    return of();
  }

  onTable(id: string) {
    this.router.navigate([`tables/${id}`]);
  }
}
