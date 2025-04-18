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
    password: '12345', // Cambiar por la contraseña configurada en tu MySQL
    database: 'salud_total_db'
});

db.connect(err => {
    if (err) {
        console.error('Error conectando a la base de datos:', err);
        return;
    }
    console.log('Conexión a la base de datos exitosa');
});

// Registro de un paciente (primero como persona, luego como paciente)
app.post('/register', (req, res) => {
    const { nombre_completo, dni, sexo, email, password } = req.body;

    // Validar datos recibidos
    if (!nombre_completo || !dni || !sexo || !email || !password) {
        res.status(400).send('Faltan datos necesarios para el registro');
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

        const id_persona = result.insertId; // Obtener el ID generado para la persona

        // Insertar en la tabla `pacientes` utilizando el ID de persona
        const sqlPaciente = "INSERT INTO pacientes (id_persona, email, password) VALUES (?, ?, ?)";
        db.query(sqlPaciente, [id_persona, email, password], (err, result) => {
            if (err) {
                console.error('Error al registrar paciente:', err);
                res.status(500).send('Error al registrar paciente');
                return;
            }

            res.send('Paciente registrado exitosamente');
        });
    });
});

// Iniciar servidor
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
