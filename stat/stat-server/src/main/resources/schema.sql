CREATE TABLE IF NOT EXISTS stats (
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
app VARCHAR(30) NOT NULL,
uri VARCHAR(1023) NOT NULL,
ip VARCHAR(1023) NOT NULL,
timestamp VARCHAR(30) NOT NULL
);