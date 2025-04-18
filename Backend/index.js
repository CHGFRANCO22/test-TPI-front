const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');
const bcrypt = require('bcrypt'); // Para encriptar contraseñas

const app = express();
app.use(cors());
app.use(express.json()); // Permitir parsing de JSON en las solicitudes

// Conexión a la base de datos MySQL
const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '12345', // Cambia por tu contraseña configurada en MySQL
    database: 'salud_total_db'
});

db.connect(err => {
    if (err) {
        console.error('Error conectando a la base de datos:', err);
        return;
    }
    console.log('Conexión a la base de datos exitosa');
});

// Registro de una persona y paciente
app.post('/register', async (req, res) => {
    console.log('Datos recibidos para registro:', req.body);

    const { nombre_completo, dni, sexo, email, password } = req.body;

    if (!nombre_completo || !dni || !sexo || !email || !password) {
        res.status(400).send('Faltan datos necesarios para el registro');
        return;
    }

    try {
        const checkDniQuery = "SELECT * FROM persona WHERE dni = ?";
        db.query(checkDniQuery, [dni], (err, results) => {
            if (err) {
                console.error('Error al verificar el DNI:', err);
                res.status(500).send('Error al verificar el DNI');
                return;
            }

            if (results.length > 0) {
                res.status(400).send('El DNI ya está registrado');
            } else {
                const sqlPersona = "INSERT INTO persona (nombre_completo, dni, sexo) VALUES (?, ?, ?)";
                db.query(sqlPersona, [nombre_completo, dni, sexo], async (err, result) => {
                    if (err) {
                        console.error('Error al registrar persona:', err);
                        res.status(500).send('Error al registrar persona');
                        return;
                    }

                    const id_persona = result.insertId;
                    console.log('Persona registrada con ID:', id_persona);

                    const hashedPassword = await bcrypt.hash(password, 10);

                    const sqlPaciente = "INSERT INTO pacientes (id_persona, email, password) VALUES (?, ?, ?)";
                    db.query(sqlPaciente, [id_persona, email, hashedPassword], (err, result) => {
                        if (err) {
                            console.error('Error al registrar paciente:', err);
                            res.status(500).send('Error al registrar paciente');
                        } else {
                            console.log('Paciente registrado con éxito:', { email });
                            res.send('Paciente registrado exitosamente');
                        }
                    });
                });
            }
        });
    } catch (error) {
        console.error('Error en el proceso de registro:', error);
        res.status(500).send('Error en el servidor');
    }
});

// Inicio de sesión
app.post('/login', async (req, res) => {
    console.log('Datos recibidos para login:', req.body);

    const { email, password } = req.body;

    if (!email || !password) {
        res.status(400).send('Correo y contraseña son requeridos');
        return;
    }

    const sql = "SELECT * FROM pacientes WHERE email = ?";
    db.query(sql, [email], async (err, results) => {
        if (err) {
            console.error('Error al verificar credenciales:', err);
            res.status(500).send('Error en el servidor');
            return;
        }

        if (results.length > 0) {
            const match = await bcrypt.compare(password, results[0].password);
            if (match) {
                console.log('Inicio de sesión exitoso para:', email);
                res.send({ message: 'Inicio de sesión exitoso', id_paciente: results[0].id_persona });
            } else {
                console.log('Credenciales incorrectas para:', email);
                res.status(401).send('Correo o contraseña incorrectos');
            }
        } else {
            console.log('Correo no registrado:', email);
            res.status(401).send('Correo o contraseña incorrectos');
        }
    });
});

// Otros endpoints (servicios, turnos y contacto) permanecen como en tu código original...

// Iniciar servidor
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
