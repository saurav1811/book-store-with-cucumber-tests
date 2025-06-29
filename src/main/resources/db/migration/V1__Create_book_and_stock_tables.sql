CREATE TABLE book(
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    name VARCHAR(255),
    author VARCHAR(255),
    price NUMBER(5,2)
);

CREATE TABLE stock(
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    book_id UUID,
    quantity INTEGER,
    FOREIGN KEY (book_id) REFERENCES book(id)
);
