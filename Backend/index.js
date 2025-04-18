const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json()); // Permitir parsing de JSON en las solicitudes

// Conexión a la base de datos MySQL
const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '12345', // Cambiar por la contraseña de tu MySQL
    database: 'salud_total_db'
});

db.connect(err => {
    if (err) {
        console.error('Error conectando a la base de datos:', err);
        return;
    }
    console.log('Conexión a la base de datos exitosa');
});

// Registro de una persona
app.post('/register', (req, res) => {
    const { nombre_completo, dni, sexo } = req.body;

    // Validar datos recibidos
    if (!nombre_completo || !dni || !sexo) {
        res.status(400).send('Faltan datos necesarios para el registro de persona');
        return;
    }

    // Insertar en la tabla `persona`
    const sqlPersona = "INSERT INTO persona (nombre_completo, dni, sexo) VALUES (?, ?, ?)";
    db.query(sqlPersona, [nombre_completo, dni, sexo], (err, result) => {
        if (err) {
            console.error('Error al registrar persona:', err);
            res.status(500).send('Error al registrar persona');
            return;
        }

        res.send('Persona registrada exitosamente');
    });
});

// Obtener todas las personas
app.get('/personas', (req, res) => {
    const sql = "SELECT * FROM persona";
    db.query(sql, (err, results) => {
        if (err) {
            res.status(500).send('Error al obtener personas');
            console.error(err);
        } else {
            res.json(results);
        }
    });
});

// Iniciar servidor
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
