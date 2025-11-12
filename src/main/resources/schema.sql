CREATE TABLE IF NOT EXISTS product (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    category VARCHAR(255),
    price DOUBLE,
    available BOOLEAN
);

CREATE TABLE IF NOT EXISTS movie (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    director VARCHAR(255),
    release_year INT,
    genre VARCHAR(255),
    rating DOUBLE
);

CREATE TABLE IF NOT EXISTS library (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    location VARCHAR(255),
    book_count INT,
    librarian VARCHAR(255),
    is_public BOOLEAN
);