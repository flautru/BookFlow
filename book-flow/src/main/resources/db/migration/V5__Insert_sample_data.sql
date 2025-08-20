-- V5__Insert_sample_data.sql

-- ===============================
-- GENRES
-- ===============================
INSERT INTO genres (name, description) VALUES
('Science Fiction', 'Livres de science-fiction et anticipation'),
('Fantasy', 'Romans de fantasy et heroic fantasy'),
('Mystery', 'Romans policiers et mystères'),
('Romance', 'Romans sentimentaux'),
('Thriller', 'Romans à suspense'),
('Biography', 'Biographies et mémoires'),
('Classic Literature', 'Œuvres littéraires classiques'),
('Horror', 'Romans d''horreur et épouvante'),
('Adventure', 'Romans d''aventure'),
('Historical Fiction', 'Romans historiques'),
('Philosophy', 'Ouvrages de philosophie'),
('Young Adult', 'Littérature jeunesse et jeunes adultes');

-- ===============================
-- AUTEURS
-- ===============================
INSERT INTO authors (first_name, last_name, birth_date, nationality) VALUES
('Isaac', 'Asimov', '1920-01-02', 'American'),
('J.R.R.', 'Tolkien', '1892-01-03', 'British'),
('Agatha', 'Christie', '1890-09-15', 'British'),
('Stephen', 'King', '1947-09-21', 'American'),
('Jane', 'Austen', '1775-12-16', 'British'),
('George', 'Orwell', '1903-06-25', 'British'),
('Arthur Conan', 'Doyle', '1859-05-22', 'British'),
('Jules', 'Verne', '1828-02-08', 'French'),
('Virginia', 'Woolf', '1882-01-25', 'British'),
('Frank', 'Herbert', '1920-10-08', 'American'),
('Ray', 'Bradbury', '1920-08-22', 'American'),
('Ursula K.', 'Le Guin', '1929-10-21', 'American'),
('Terry', 'Pratchett', '1948-04-28', 'British'),
('Neil', 'Gaiman', '1960-11-10', 'British'),
('Brandon', 'Sanderson', '1975-12-19', 'American');

-- ===============================
-- LIVRES
-- ===============================
INSERT INTO books (title, subtitle, isbn, description, publication_year) VALUES
-- Science Fiction
('Foundation', NULL, '9780553293357', 'Premier tome de la saga Foundation', 1951),
('Dune', NULL, '9780441172719', 'Épopée science-fiction sur la planète Arrakis', 1965),
('Fahrenheit 451', NULL, '9781451673319', 'Dystopie sur la censure des livres', 1953),
('The Left Hand of Darkness', NULL, '9780441478125', 'Exploration du genre sur une planète alien', 1969),
('I, Robot', NULL, '9780553382563', 'Recueil de nouvelles sur les robots', 1950),

-- Fantasy
('The Lord of the Rings', 'The Fellowship of the Ring', '9780547928210', 'Premier tome du Seigneur des Anneaux', 1954),
('The Lord of the Rings', 'The Two Towers', '9780547928203', 'Deuxième tome du Seigneur des Anneaux', 1954),
('The Lord of the Rings', 'The Return of the King', '9780547928197', 'Dernier tome du Seigneur des Anneaux', 1955),
('Good Omens', NULL, '9780060853976', 'Comédie apocalyptique', 1990),
('The Way of Kings', NULL, '9780765326355', 'Premier tome des Archives de Roshar', 2010),
('The Color of Magic', NULL, '9780552166591', 'Premier tome du Disque-monde', 1983),

-- Mystery/Thriller
('Murder on the Orient Express', NULL, '9780062693662', 'Hercule Poirot enquête dans l''Orient Express', 1934),
('The Adventures of Sherlock Holmes', NULL, '9780486474915', 'Recueil d''enquêtes de Sherlock Holmes', 1892),
('And Then There Were None', NULL, '9780062073488', 'Dix petits nègres - chef-d''œuvre du polar', 1939),

-- Horror
('The Shining', NULL, '9780307743657', 'Roman d''horreur dans un hôtel isolé', 1977),
('Pet Sematary', NULL, '9780307743688', 'Horror sur la résurrection', 1983),

-- Classic Literature
('Pride and Prejudice', NULL, '9780141439518', 'Roman classique britannique', 1813),
('1984', NULL, '9780452284234', 'Dystopie totalitaire', 1949),
('Mrs. Dalloway', NULL, '9780156628709', 'Roman moderniste sur une journée à Londres', 1925),

-- Adventure
('Twenty Thousand Leagues Under the Sea', NULL, '9780486266299', 'Aventure sous-marine avec le capitaine Nemo', 1870),
('Around the World in Eighty Days', NULL, '9780486411118', 'Course autour du monde en 80 jours', 1873);

-- ===============================
-- RELATIONS BOOK-GENRE (avec intensity)
-- ===============================
INSERT INTO book_genres (book_id, genre_id, intensity) VALUES
-- Foundation (SF PRIMARY)
(1, 1, 'PRIMARY'),
-- Dune (SF PRIMARY)
(2, 1, 'PRIMARY'),
-- Fahrenheit 451 (SF PRIMARY, Classic SECONDARY)
(3, 1, 'PRIMARY'), (3, 7, 'SECONDARY'),
-- The Left Hand of Darkness (SF PRIMARY)
(4, 1, 'PRIMARY'),
-- I, Robot (SF PRIMARY)
(5, 1, 'PRIMARY'),
-- LOTR Fellowship (Fantasy PRIMARY, Adventure SECONDARY)
(6, 2, 'PRIMARY'), (6, 9, 'SECONDARY'),
-- LOTR Two Towers (Fantasy PRIMARY, Adventure SECONDARY)
(7, 2, 'PRIMARY'), (7, 9, 'SECONDARY'),
-- LOTR Return (Fantasy PRIMARY, Adventure SECONDARY)
(8, 2, 'PRIMARY'), (8, 9, 'SECONDARY'),
-- Good Omens (Fantasy PRIMARY)
(9, 2, 'PRIMARY'),
-- Way of Kings (Fantasy PRIMARY)
(10, 2, 'PRIMARY'),
-- Color of Magic (Fantasy PRIMARY)
(11, 2, 'PRIMARY'),
-- Orient Express (Mystery PRIMARY)
(12, 3, 'PRIMARY'),
-- Sherlock Holmes (Mystery PRIMARY, Classic SECONDARY)
(13, 3, 'PRIMARY'), (13, 7, 'SECONDARY'),
-- And Then There Were None (Mystery PRIMARY, Thriller SECONDARY)
(14, 3, 'PRIMARY'), (14, 5, 'SECONDARY'),
-- The Shining (Horror PRIMARY, Thriller SECONDARY)
(15, 8, 'PRIMARY'), (15, 5, 'SECONDARY'),
-- Pet Sematary (Horror PRIMARY)
(16, 8, 'PRIMARY'),
-- Pride and Prejudice (Romance PRIMARY, Classic SECONDARY)
(17, 4, 'PRIMARY'), (17, 7, 'SECONDARY'),
-- 1984 (SF PRIMARY, Classic SECONDARY, Philosophy SECONDARY)
(18, 1, 'PRIMARY'), (18, 7, 'SECONDARY'), (18, 11, 'SECONDARY'),
-- Mrs. Dalloway (Classic Literature PRIMARY)
(19, 7, 'PRIMARY'),
-- Twenty Thousand Leagues (Adventure PRIMARY, SF SECONDARY)
(20, 9, 'PRIMARY'), (20, 1, 'SECONDARY'),
-- Around the World (Adventure PRIMARY, Classic SECONDARY)
(21, 9, 'PRIMARY'), (21, 7, 'SECONDARY');

-- ===============================
-- RELATIONS AUTHOR-ROLE (avec contribution_type)
-- ===============================
INSERT INTO author_roles (book_id, author_id, contribution_type) VALUES
-- Isaac Asimov
(1, 1, 'AUTHOR'),  -- Foundation
(5, 1, 'AUTHOR'),  -- I, Robot
-- J.R.R. Tolkien
(6, 2, 'AUTHOR'),  -- LOTR Fellowship
(7, 2, 'AUTHOR'),  -- LOTR Two Towers
(8, 2, 'AUTHOR'),  -- LOTR Return
-- Agatha Christie
(12, 3, 'AUTHOR'), -- Orient Express
(14, 3, 'AUTHOR'), -- And Then There Were None
-- Stephen King
(15, 4, 'AUTHOR'), -- The Shining
(16, 4, 'AUTHOR'), -- Pet Sematary
-- Jane Austen
(17, 5, 'AUTHOR'), -- Pride and Prejudice
-- George Orwell
(18, 6, 'AUTHOR'), -- 1984
-- Arthur Conan Doyle
(13, 7, 'AUTHOR'), -- Sherlock Holmes
-- Jules Verne
(20, 8, 'AUTHOR'), -- Twenty Thousand Leagues
(21, 8, 'AUTHOR'), -- Around the World
-- Virginia Woolf
(19, 9, 'AUTHOR'), -- Mrs. Dalloway
-- Frank Herbert
(2, 10, 'AUTHOR'), -- Dune
-- Ray Bradbury
(3, 11, 'AUTHOR'), -- Fahrenheit 451
-- Ursula K. Le Guin
(4, 12, 'AUTHOR'), -- Left Hand of Darkness
-- Terry Pratchett & Neil Gaiman (collaboration)
(9, 13, 'AUTHOR'), -- Good Omens
(9, 14, 'CO_AUTHOR'), -- Good Omens
-- Terry Pratchett
(11, 13, 'AUTHOR'), -- Color of Magic
-- Brandon Sanderson
(10, 15, 'AUTHOR'); -- Way of Kings