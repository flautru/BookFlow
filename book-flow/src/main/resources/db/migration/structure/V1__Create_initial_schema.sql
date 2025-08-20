-- Table Users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    member_number BIGINT UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    address VARCHAR(500),
    birth_date DATE,
    user_type VARCHAR(20) NOT NULL CHECK (user_type IN ('CLASSIC', 'STUDENT', 'PROFESSOR'))
);

-- Table Authors
CREATE TABLE authors (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE,
    nationality VARCHAR(100)
);

-- Table Genres
CREATE TABLE genres (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT
);

-- Table Books
CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    isbn VARCHAR(13) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    subtitle VARCHAR(255),
    description TEXT,
    publication_year INTEGER
);

-- Table Physical Books
CREATE TABLE physical_books (
    id BIGSERIAL PRIMARY KEY,
    book_id BIGSERIAL,
    barcode VARCHAR(255) UNIQUE NOT NULL,
    condition VARCHAR(20) NOT NULL CHECK (condition IN ('NEW', 'GOOD', 'FAIR', 'POOR', 'DAMAGED')),
    location VARCHAR(255),
    acquisition_date DATE
);

-- Tables de liaison
CREATE TABLE author_roles (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGSERIAL,
    book_id BIGSERIAL,
    contribution_type VARCHAR(20) NOT NULL CHECK (contribution_type IN ('AUTHOR', 'CO_AUTHOR', 'TRANSLATOR', 'ILLUSTRATOR'))
);

CREATE TABLE book_genres (
    id BIGSERIAL PRIMARY KEY,
    book_id BIGSERIAL,
    genre_id BIGSERIAL,
    intensity VARCHAR(20) NOT NULL CHECK (intensity IN ('PRIMARY', 'SECONDARY'))
);