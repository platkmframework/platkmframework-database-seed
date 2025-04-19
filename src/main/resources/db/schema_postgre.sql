DROP TABLE IF EXISTS multa;
DROP TABLE IF EXISTS vehiculo;
DROP TABLE IF EXISTS persona;

CREATE TABLE persona (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100)
);

CREATE TABLE vehiculo (
    id SERIAL PRIMARY KEY,
    marca VARCHAR(100),
    id_persona INT,
    FOREIGN KEY (id_persona) REFERENCES persona(id)
);

CREATE TABLE multa (
    id SERIAL PRIMARY KEY,
    fecha DATE,
    id_vehiculo INT,
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculo(id)
);
