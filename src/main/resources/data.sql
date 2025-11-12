truncate table product;
INSERT INTO product (name, category, price, available) VALUES ('Wireless Mouse', 'Electronics', 799.00, true);
INSERT INTO product (name, category, price, available) VALUES ('Bluetooth Speaker', 'Electronics', 1499.00, true);
INSERT INTO product (name, category, price, available) VALUES ('Cotton T-Shirt', 'Apparel', 499.00, true);
INSERT INTO product (name, category, price, available) VALUES ('Ceramic Mug', 'Home & Kitchen', 299.00, true);
INSERT INTO product (name, category, price, available) VALUES ('Notebook', 'Stationery', 99.00, true);

INSERT INTO movie (title, director, release_year, genre, rating) VALUES ('Inception', 'Christopher Nolan', 2010, 'Sci-Fi', 8.8);
INSERT INTO movie (title, director, release_year, genre, rating) VALUES ('The Shawshank Redemption', 'Frank Darabont', 1994, 'Drama', 9.3);
INSERT INTO movie (title, director, release_year, genre, rating) VALUES ('The Godfather', 'Francis Ford Coppola', 1972, 'Crime', 9.2);
INSERT INTO movie (title, director, release_year, genre, rating) VALUES ('The Dark Knight', 'Christopher Nolan', 2008, 'Action', 9.0);
INSERT INTO movie (title, director, release_year, genre, rating) VALUES ('The Lord of the Rings: The Return of the King', 'Peter Jackson', 2003, 'Action', 8.9);

INSERT INTO library (name, location, book_count, librarian, is_public) VALUES ('Central Library', 'City Center', 1000, 'John Doe', true);
INSERT INTO library (name, location, book_count, librarian, is_public) VALUES ('Science Library', 'Science Building', 500, 'Jane Smith', false);
INSERT INTO library (name, location, book_count, librarian, is_public) VALUES ('Children Library', 'Children Building', 200, 'Alice Johnson', true);
INSERT INTO library (name, location, book_count, librarian, is_public) VALUES ('History Library', 'History Building', 300, 'Bob Brown', false);
INSERT INTO library (name, location, book_count, librarian, is_public) VALUES ('Art Library', 'Art Building', 400, 'Charlie Davis', true);
