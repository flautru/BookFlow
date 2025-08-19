ALTER TABLE author_roles
ADD CONSTRAINT uk_author_roles_unique
UNIQUE (book_id, author_id, contribution_type);

ALTER TABLE book_genres
ADD CONSTRAINT uk_book_genres_unique
UNIQUE (book_id, genre_id);