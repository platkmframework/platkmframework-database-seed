IF OBJECT_ID('multa', 'U') IS NOT NULL DROP TABLE multa;
IF OBJECT_ID('vehiculo', 'U') IS NOT NULL DROP TABLE vehiculo;
IF OBJECT_ID('persona', 'U') IS NOT NULL DROP TABLE persona;

CREATE TABLE persona (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100)
);

CREATE TABLE vehiculo (
    id INT IDENTITY(1,1) PRIMARY KEY,
    marca NVARCHAR(100),
    id_persona INT,
    FOREIGN KEY (id_persona) REFERENCES persona(id)
);

CREATE TABLE multa (
    id INT IDENTITY(1,1) PRIMARY KEY,
    fecha DATE,
    id_vehiculo INT,
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculo(id)
);
