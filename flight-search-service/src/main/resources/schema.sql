CREATE TABLE flight (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    origin VARCHAR(255),
    destination VARCHAR(255),
    flight_number VARCHAR(255),
    flight_date VARCHAR(255),
    seats_available INT,
    fare DOUBLE
);
