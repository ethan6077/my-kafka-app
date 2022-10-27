-- Create declared data table
CREATE TABLE IF NOT EXISTS books
(
    id                  serial PRIMARY KEY,
    author              VARCHAR(50),
    title               VARCHAR(50),
    type                VARCHAR(50),
    pages               INT,
    release_date        DATE
);

INSERT INTO books(author, title, type, pages, release_date) VALUES ('J. K. Rowling', 'Harry Potter', 'NOVEL', 900, '2003-06-01');
