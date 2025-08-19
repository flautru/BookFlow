CREATE INDEX idx_author_roles_book_id ON author_roles(book_id);
CREATE INDEX idx_author_roles_author_id ON author_roles(author_id);
CREATE INDEX idx_book_genres_book_id ON book_genres(book_id);
CREATE INDEX idx_book_genres_genre_id ON book_genres(genre_id);
CREATE INDEX idx_physical_books_book_id ON physical_books(book_id);