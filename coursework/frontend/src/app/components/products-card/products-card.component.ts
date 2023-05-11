import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { CategoryDto } from 'src/app/models/dto/category/category-dto';
import { ProductDto } from 'src/app/models/dto/product/product-dto';

@Component({
  selector: 'app-products-card',
  templateUrl: './products-card.component.html',
  styleUrls: ['./products-card.component.css']
})
export class ProductsCardComponent {
  @Input() product: ProductDto;
  @Input() category: CategoryDto;

  @Output() onClick = new EventEmitter<string>();

  constructor(private sanitizer: DomSanitizer) { }

  arrayBufferToUrl(arrayBuffer: ArrayBuffer): string {
    const altUrl = 'https://via.placeholder.com/100';
    if (arrayBuffer === null) {
      return altUrl;
    }
    return `data:image/png;base64,${arrayBuffer}`
  }

  cardClick() {
    this.onClick.emit(this.product.id);
  }
}
