import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookService } from '../../services/book.service';

// Angular Material Imports
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatChipsModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {
  bookService = inject(BookService);

  ngOnInit() {
    this.loadBooks();
  }

  loadBooks() {
    this.bookService.loadBooks();
  }

  trackByBookId(index: number, book: any) {
    return book.id;
  }

  getGenreColor(intensity: string): string {
  switch(intensity) {
    case 'PRIMARY': return 'primary';
    case 'SECONDARY': return 'accent';
    default: return 'basic';
  }
}

getGenreClass(intensity: string): string {
  return intensity === 'PRIMARY' ? 'genre-primary' : 'genre-secondary';
}
}
