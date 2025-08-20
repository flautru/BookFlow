import { Injectable, signal, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Book } from '../models';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api';

  private books = signal<Book[]>([]);
  private loading = signal(false);
  private error = signal<string | null>(null);

  readonly books$ = this.books.asReadonly();
  readonly isLoading$ = this.loading.asReadonly();
  readonly error$ = this.error.asReadonly();

  loadBooks() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<Book[]>(`${this.baseUrl}/books`).subscribe({
      next: (books) => {
        this.books.set(books);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.message);
        this.loading.set(false);
      }
    });
  }
}
