INSERT INTO book(name, author, price) VALUES ('Harry Potter: The Chamber of Secrets', 'J K Rowling', 150.75);
INSERT INTO book(name, author, price) VALUES ('Sherlock Holmes: The Hound of Baskervilles', 'Arthur Conan Doyle', 220.15);
INSERT INTO book(name, author, price) VALUES ('The Alchemist', 'Paulo Coelho', 125.45);

INSERT INTO stock(book_id, quantity) VALUES ((SELECT id FROM book WHERE name='Harry Potter: The Chamber of Secrets'), 6);
INSERT INTO stock(book_id, quantity) VALUES ((SELECT id FROM book WHERE name='Sherlock Holmes: The Hound of Baskervilles'), 7);
INSERT INTO stock(book_id, quantity) VALUES ((SELECT id FROM book WHERE name='The Alchemist'), 2);
