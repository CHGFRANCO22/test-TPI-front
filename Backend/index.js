const express = require('express');
const mysql = require('mysql');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json()); // Permitir parsing de JSON en las solicitudes

// Conexión a la base de datos MySQL
const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',      // Cambiar por tu usuario de MySQL
    password: '',      // Cambiar por tu contraseña de MySQL
    database: 'salud_total' // Cambiar por el nombre de tu base de datos
});

db.connect(err => {
    if (err) {
        console.error('Error conectando a la base de datos:', err);
        return;
    }
    console.log('Conexión a la base de datos exitosa');
});

// Endpoints del servidor

// Registro de usuarios
app.post('/register', (req, res) => {
    const { nombre, email, password } = req.body;
    const sql = "INSERT INTO usuarios (nombre, email, password) VALUES (?, ?, ?)";
    db.query(sql, [nombre, email, password], (err, result) => {
        if (err) {
            res.status(500).send('Error al registrar usuario');
            console.error(err);
        } else {
            res.send('Usuario registrado exitosamente');
        }
    });
});

// Inicio de sesión
app.post('/login', (req, res) => {
    const { email, password } = req.body;
    const sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";
    db.query(sql, [email, password], (err, results) => {
        if (err) {
            res.status(500).send('Error en el inicio de sesión');
            console.error(err);
        } else if (results.length > 0) {
            res.send('Inicio de sesión exitoso');
        } else {
            res.status(401).send('Credenciales incorrectas');
        }
    });
});

// Obtener turnos
app.get('/turnos', (req, res) => {
    const sql = "SELECT * FROM turnos";
    db.query(sql, (err, results) => {
        if (err) {
            res.status(500).send('Error al obtener turnos');
            console.error(err);
        } else {
            res.json(results);
        }
    });
});

// Crear turno
app.post('/turnos', (req, res) => {
    const { especialidad, profesional, fecha, hora, paciente_id } = req.body;
    const sql = "INSERT INTO turnos (especialidad, profesional, fecha, hora, paciente_id) VALUES (?, ?, ?, ?, ?)";
    db.query(sql, [especialidad, profesional, fecha, hora, paciente_id], (err, result) => {
        if (err) {
            res.status(500).send('Error al crear turno');
            console.error(err);
        } else {
            res.send('Turno creado exitosamente');
        }
    });
});

// Iniciar servidor
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
