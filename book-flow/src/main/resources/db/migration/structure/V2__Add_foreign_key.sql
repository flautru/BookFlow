-- Physical Books → Books relationship
ALTER TABLE physical_books
ADD CONSTRAINT fk_physical_books_book
FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE;

-- Author Roles → Books relationship
ALTER TABLE author_roles
ADD CONSTRAINT fk_author_roles_book
FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE;

-- Author Roles → Authors relationship
ALTER TABLE author_roles
ADD CONSTRAINT fk_author_roles_author
FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE;

-- Book Genres → Books relationship
ALTER TABLE book_genres
ADD CONSTRAINT fk_book_genres_book
FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE;

-- Book Genres → Genres relationship
ALTER TABLE book_genres
ADD CONSTRAINT fk_book_genres_genre
FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE;